package com.example.shoppinglist.model

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.shoppinglist.BuildConfig
import java.io.*

class DBHandler(private val mContext: Context) : SQLiteOpenHelper(mContext, DB_NAME, null, DB_VERSION) {

    private var mDataBase: SQLiteDatabase? = null
    private var mNeedUpdate = false
    private val DB_PATH: String;

    companion object {
        private val DB_VERSION = BuildConfig.VERSION_CODE
        private val DB_NAME = "ShoppingList.db"
    }

    init {
        if (android.os.Build.VERSION.SDK_INT >= 17)
            DB_PATH = mContext.applicationInfo.dataDir + "/databases/"
        else
            DB_PATH = "/data/data/" + mContext.packageName + "/databases/"

        copyDatabase()
        this.writableDatabase
    }

    @Throws(IOException::class)
    fun updateDataBase() {
        if (mNeedUpdate) {
            val dbFile = File(DB_PATH + DB_NAME)
            if (dbFile.exists()) {
                dbFile.delete()
            }
            copyDatabase()
            mNeedUpdate = false
        }
    }

    private fun checkDatabase(): Boolean {
        val dbFile = File(DB_PATH + DB_NAME)
        return dbFile.exists()
    }

    private fun copyDatabase() {
        if (!checkDatabase()) {
            this.writableDatabase
            this.close()
            try {
                copyDBFile()
            } catch (mIOException: IOException) {
                throw Error("ErrorCopyingDataBase")
            }
        }
    }

    @Throws(IOException::class)
    private fun copyDBFile() {
        val mInput = mContext.assets.open(DB_NAME)
        //InputStream mInput = mContext.getResources().openRawResource(R.raw.info);
        val mOutput = FileOutputStream(DB_PATH + DB_NAME)
        val mBuffer = ByteArray(1024)
        var mLength: Int
        mLength = 1
        var size: Int = 0
        while (mLength > 0) {
            mLength = mInput.read(mBuffer)
            if (mLength > 0) {
                mOutput.write(mBuffer, 0, mLength)
                size += mLength
            }
        }
        mOutput.flush()
        mOutput.close()
        mInput.close()
    }

    @Throws(SQLException::class)
    fun openDatabase(): Boolean {
        mDataBase = SQLiteDatabase.openDatabase(
            DB_PATH + DB_NAME, null,
            SQLiteDatabase.CREATE_IF_NECESSARY
        )
        return mDataBase != null
    }

    @Synchronized
    override fun close() {
        if (mDataBase != null)
            mDataBase!!.close()
        super.close()
    }

    override fun onCreate(db: SQLiteDatabase) {
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (newVersion > oldVersion) {
            mNeedUpdate = true
            updateDataBase()
        }
    }

    fun getShoppingProjectsList(archived: String): MutableList<ShoppingProject> {
        val projectList = mutableListOf<ShoppingProject>()
        val selectionArgs = arrayOf(archived)
        val data = mDataBase?.rawQuery(
            "SELECT * FROM Projects WHERE ARCHIVED = ? ORDER BY DATE ASC",
            selectionArgs
        )
        if (data != null) {
            while (data.moveToNext()) {
                val id = data.getInt(0)
                val title = data.getString(1)
                val date = data.getLong(2)
                val archived = data.getInt(3)
                val item = ShoppingProject(id, title, date, archived)
                projectList.add(item)
            }
        }
        return projectList
    }

    fun getShoppingProjectItemsList(projectId: String): MutableList<Item> {
        val itemList = mutableListOf<Item>()
        val selectionArgs = arrayOf(projectId)
        val data = mDataBase?.rawQuery(
            "SELECT * FROM Items WHERE PROJECT_ID=? ORDER BY ID ASC",
            selectionArgs
        )
        if (data != null) {
            while (data.moveToNext()) {
                val id = data.getInt(0)
                val projectId = data.getInt(1)
                val title = data.getString(2)
                val completed = data.getInt(3)
                val item = Item(
                    id, projectId,
                    title, completed
                )
                itemList.add(item)
            }
        }
        return itemList
    }

    fun insertProject(sp: ShoppingProject): Int {
        var projectId = -1
        if (mDataBase != null) {
            mDataBase?.beginTransaction();
            try {
                val sql = "INSERT INTO Projects(TITLE, DATE, ARCHIVED) VALUES (?,?,?)"
                val stmt = mDataBase?.compileStatement(sql)
                stmt?.bindString(1, sp.name)
                stmt?.bindString(2, sp.date.time.toString())
                stmt?.bindString(3, sp.archived.toString())
                stmt?.execute()
                stmt?.clearBindings()
                mDataBase?.setTransactionSuccessful();
            } catch (mIOException: IOException) {
                throw  Error("Blad przy wstawianiu")
            } finally {
                mDataBase?.endTransaction();
            }

            val selectionArgs = arrayOf(sp.name,sp.date.time.toString())
            val data = mDataBase?.rawQuery(
                "SELECT ID FROM Projects WHERE TITLE = ? AND DATE = ?", selectionArgs)
            if (data!=null) {
                data.moveToFirst()//.moveToNext()
                projectId = data.getInt(0)
            }
        }
        return projectId
    }

    fun insertItem(im: Item): Int {
        var itemId = -1
        if (mDataBase != null) {
            mDataBase?.beginTransaction();
            try {
                val sql = "INSERT INTO Items(PROJECT_ID, NAME, COMPLETED) VALUES (?,?,?)"
                val stmt = mDataBase?.compileStatement(sql)
                stmt?.bindString(1, im.projectId.toString())
                stmt?.bindString(2, im.name)
                stmt?.bindString(3, im.completed.toString())
                stmt?.execute()
                stmt?.clearBindings()
                mDataBase?.setTransactionSuccessful();
            } catch (mIOException: IOException) {
                throw  Error("Blad przy wstawianiu")
            } finally {
                mDataBase?.endTransaction()
            }
            val selectionArgs = arrayOf(im.name,im.projectId.toString())
            val data = mDataBase?.rawQuery(
                "SELECT ID FROM Items WHERE NAME=? AND PROJECT_ID=?", selectionArgs)
            if (data!=null) {
                data.moveToFirst()
                itemId = data.getInt(0)
            }
        }
        return itemId
    }

    fun updateItem(im: Item) {
        try {
            val values = ContentValues()
            values.put("COMPLETED", im.completed.toString())
            val retVal = mDataBase!!.update(
                "Items", values, "ID= ? AND PROJECT_ID = ?",
                arrayOf(im.id.toString(), im.projectId.toString())
            )
        } catch (mIOException: IOException) {
            throw  Error("Blad przy wstawianiu")
        } finally {
        }
    }

    fun updateProject(sp: ShoppingProject) {

        try {
            val values = ContentValues()
            values.put("ARCHIVED", sp.archived.toString())
            val retVal = mDataBase!!.update(
                "Projects", values, "ID=?",
                arrayOf(sp.id.toString())
            )
        } catch (mIOException: IOException) {
            throw  Error("Blad przy wstawianiu")
        } finally {
        }
    }

    fun deleteProject(sp: ShoppingProject):Int{
        val num=mDataBase?.delete("Projects","ID = ?", arrayOf(sp.id.toString()))
        if(num!=null) return num
        return 0
    }

    fun deleteItem(im: Item):Int{
        val num=mDataBase?.delete("Items","ID = ?", arrayOf(im.id.toString()))
        if(num!=null) return num
        return 0
    }

}