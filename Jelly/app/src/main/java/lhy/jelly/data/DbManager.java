package lhy.jelly.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lhy.jelly.data.local.gen.DaoMaster;
import lhy.jelly.data.local.gen.DaoSession;
import lhy.jelly.data.local.gen.UserDao;

/**
 * Created by Liheyu on 2017/8/15.
 * Email:liheyu999@163.com
 */


public class DbManager {

    private static final String DB_NAME = "jelly.db";
    private static DbManager instance;
    private DaoSession mDaoSession;

    private DbManager(Context context) {
        init(context);
    }

    public static DbManager getInstance(Context context) {
        if (instance == null) {
            synchronized (DbManager.class) {
                if (instance == null) {
                    instance = new DbManager(context);
                }
            }
        }
        return instance;
    }

    private void init(Context context) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, DB_NAME);
        SQLiteDatabase database = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(database);
        mDaoSession = daoMaster.newSession();
    }

    public UserDao getUserDao() {
        return mDaoSession.getUserDao();
    }
}
