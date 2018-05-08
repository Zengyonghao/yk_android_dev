package com.zplh.zplh_android_yk.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.zplh.zplh_android_yk.bean.My_list_da_bean;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/15.
 */

public class MyList_Dao {
    private MyListDB dao=null;
    public MyList_Dao(Context context){
        dao = new MyListDB(context);
    }
    //实现对该数据库的增加
    public void addPerson(My_list_da_bean bean){
        //获取操作实例
        SQLiteDatabase db = dao.getWritableDatabase();
        //此方法推荐使用
        String sqlStr="insert into list_mydata(login_id,times,ali_add_num_s,ali_add_num_e,ali_version,interval_time,contact_verify_msg)values(?,?,?,?,?,?,?)";
        //执行SQL语句
        db.execSQL(sqlStr,new Object[]{bean.getLogin_id(),bean.getTimes(),bean.getAli_add_num_s(),bean.getAli_add_num_e(),bean.getAli_version(),bean.getInterval_time(),bean.getContact_verify_msg()});
        //关闭数据库
        db.close();
    }


    /**
     * 通过login_id去删除表中对应的某一条数据
     * @param login_id
     */
    public void deletlog_id(int login_id){
        SQLiteDatabase db=dao.getWritableDatabase();
        //创建SQL字符串
        String sqlStr="delete from list_mydata where login_id=?";
        db.execSQL(sqlStr,new String[]{String.valueOf(login_id)});
        //关闭数据库
        db.close();
    }
    //实现对数据库所有数据的删除
    public void deleteAll(){
        //获取数据库操作的实例
        SQLiteDatabase db=dao.getWritableDatabase();
        //创建SQL字符串
        String sqlStr="delete from list_mydata";
        db.execSQL(sqlStr);
        //关闭数据库
        db.close();
    }
    //实现对数据库的修改
    public void updatePerson(My_list_da_bean bean){
        //获取数据库的操作实例
        SQLiteDatabase db=dao.getWritableDatabase();
        //创建SQl字符串
        String sqlStr="update list_mydata set times=?,ali_add_num_s=?,ali_add_num_e=?,ali_version=?,interval_time=?,contact_verify_msg=? where login_id=?";
        //执行SQL语句
        db.execSQL(sqlStr, new Object[]{bean.getTimes(),bean.getAli_add_num_s(),bean.getAli_add_num_e(),bean.getAli_version(),bean.getInterval_time(),bean.getContact_verify_msg(),bean.getLogin_id()});
        //关闭数据库
        db.close();
    }
    //实现对数据库的查询
    public List<My_list_da_bean> selectPerson(){
        //创建集合
        List<My_list_da_bean> renwuBeans=new ArrayList<My_list_da_bean>();
        //获取数据库操作实例
        SQLiteDatabase db=dao.getReadableDatabase();
        //创建Cursor对象
        Cursor cursor=null;
        try {
            cursor=db.query("list_mydata",null,null,null,null,null,"login_id DESC");
            while(cursor.moveToNext()){
                int login_id=cursor.getInt(cursor.getColumnIndex("login_id"));
                String times=cursor.getString(cursor.getColumnIndex("times"));
                int ali_add_num_s=cursor.getInt(cursor.getColumnIndex("ali_add_num_s"));
                int ali_add_num_e=cursor.getInt(cursor.getColumnIndex("ali_add_num_e"));
                int ali_version=cursor.getInt(cursor.getColumnIndex("ali_version"));
                String interval_time=cursor.getString(cursor.getColumnIndex("interval_time"));
                String contact_verify_msg=cursor.getString(cursor.getColumnIndex("contact_verify_msg"));
                //创建Person对象
                My_list_da_bean p=new My_list_da_bean(login_id,times,ali_add_num_s,ali_add_num_e,ali_version,interval_time,contact_verify_msg);
                //将创建出来的Person对象添加到集合中去
                renwuBeans.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            //关闭相应的资源
            if(cursor!=null){
                cursor.close();
            }
            if(db!=null){
                db.close();
            }
        }
        return renwuBeans;
    }

    /**
     * 查询所有的表中的log_id
     * @return
     */
    public List<String> select_log_id(){
        List<String>stringSet=new ArrayList<String>();
        SQLiteDatabase db=dao.getWritableDatabase();
        Cursor cursor=null;
        cursor=db.query("list_mydata",null,null,null,null,null,"login_id");
        while(cursor.moveToNext()){
            String login_id=cursor.getString(cursor.getColumnIndex("login_id"));
            //将创建出来的Person对象添加到集合中去
            stringSet.add(login_id);
        }
        return stringSet;
    }

    /**
     * 查询所有时间的存储
     * @return
     */
    public List<String>select_logid_all(){
        List<String>string_list=new ArrayList<String>();
        SQLiteDatabase db=dao.getWritableDatabase();
        Cursor cursor=null;
        cursor=db.rawQuery("select * from list_mydata where times",null);
        while (cursor.moveToNext()){
            String times=cursor.getString(cursor.getColumnIndex("times"));
            string_list.add(times);

        }
        return string_list;

    }
}
