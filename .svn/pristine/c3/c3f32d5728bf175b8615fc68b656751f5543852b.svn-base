package com.zplh.zplh_android_yk.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zplh.zplh_android_yk.bean.StateRenwuBean;
import com.zplh.zplh_android_yk.db.SqliteOpend;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/6/30.
 */

public class StateDao {
    private SqliteOpend helper = null;
    /**
     * @param context
     */
    public StateDao(Context context){
        helper = new SqliteOpend(context);
    }
    //实现对该数据库的增加
    public void addPerson(StateRenwuBean stateRenwuBean){
        //获取操作实例
        SQLiteDatabase db = helper.getWritableDatabase();
        //此方法推荐使用
        String sqlStr="insert into states(id_task,login_id,result,times)values(?,?,?,?)";
        //执行SQL语句
        db.execSQL(sqlStr,new Object[]{stateRenwuBean.getId_task(),stateRenwuBean.getLogin_id(),stateRenwuBean.getResult(),stateRenwuBean.getTimes()});
        //关闭数据库
        db.close();
    }
    //实现对数据库的删除
    public void deletePerson(String id_task){
        //获取数据库操作的实例
        SQLiteDatabase db=helper.getWritableDatabase();
        //创建SQL字符串
        String sqlStr="delete from states where id_task=?";
        db.execSQL(sqlStr,new String[]{id_task});
        //关闭数据库
        db.close();
    }

    /**
     * 通过login_id去删除表中对应的某一条数据
     * @param login_id
     */
    public void deletlog_id(String login_id){
        SQLiteDatabase db=helper.getWritableDatabase();
        //创建SQL字符串
        String sqlStr="delete from states where login_id=?";
        db.execSQL(sqlStr,new String[]{login_id});
        //关闭数据库
        db.close();
    }
    //实现对数据库所有数据的删除
    public void deleteAll(){
        //获取数据库操作的实例
        SQLiteDatabase db=helper.getWritableDatabase();
        //创建SQL字符串
        String sqlStr="delete from states";
        db.execSQL(sqlStr);
        //关闭数据库
        db.close();
    }
    //实现对数据库的修改
    public void updatePerson(StateRenwuBean stateRenwuBean){
        //获取数据库的操作实例
        SQLiteDatabase db=helper.getWritableDatabase();
        //创建SQl字符串
        String sqlStr="update states set id_task=?,times=?,result=?where login_id=?";
        //执行SQL语句
        db.execSQL(sqlStr, new Object[]{stateRenwuBean.getId_task(),stateRenwuBean.getTimes(),stateRenwuBean.getResult(),stateRenwuBean.getLogin_id()});
        //关闭数据库
        db.close();
    }
    //实现对数据库的查询
    public List<StateRenwuBean> selectPerson(){
        //创建集合
        List<StateRenwuBean> renwuBeans=new ArrayList<StateRenwuBean>();
        //获取数据库操作实例
        SQLiteDatabase db=helper.getReadableDatabase();
        //创建Cursor对象
        Cursor cursor=null;
        try {
           // cursor = db.rawQuery("select * from states order by id_task",null);
            cursor=db.query("states",null,null,null,null,null,"login_id DESC");
            while(cursor.moveToNext()){
                int login_id=cursor.getInt(cursor.getColumnIndex("login_id"));
                int id_task=cursor.getInt(cursor.getColumnIndex("id_task"));
                String result=cursor.getString(cursor.getColumnIndex("result"));
                String times=cursor.getString(cursor.getColumnIndex("times"));
                //创建Person对象
                StateRenwuBean p=new StateRenwuBean(id_task,login_id,result,times);
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
        SQLiteDatabase db=helper.getWritableDatabase();
        Cursor cursor=null;
        cursor=db.query("states",null,null,null,null,null,"login_id");
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
        SQLiteDatabase db=helper.getWritableDatabase();
        Cursor cursor=null;
        cursor=db.rawQuery("select * from states where times",null);
        while (cursor.moveToNext()){
            String times=cursor.getString(cursor.getColumnIndex("times"));
            string_list.add(times);

        }
        return string_list;

   }
}
