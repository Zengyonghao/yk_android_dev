package com.zplh.zplh_android_yk.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zplh.zplh_android_yk.R;
import com.zplh.zplh_android_yk.bean.StateRenwuBean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */

public class StateAdpter extends BaseAdapter {
    private List<StateRenwuBean> list;
    private Context context;

    public StateAdpter(List<StateRenwuBean> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.state_item, null);
            viewHolder.renwu_name = (TextView) view.findViewById(R.id.renwu_name);
            viewHolder.renwu_state = (TextView) view.findViewById(R.id.renwu_state);
            viewHolder.image_state = (ImageView) view.findViewById(R.id.image_state);
            viewHolder.renwu_time = (TextView) view.findViewById(R.id.renwu_time);
            viewHolder.log_id = (TextView) view.findViewById(R.id.renwu_log_id);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (list.get(i).getId_task() == 1) {
            viewHolder.renwu_name.setText("执行任务：朋友圈点赞");
        } else if (list.get(i).getId_task() == 2) {
            viewHolder.renwu_name.setText("执行任务：朋友圈图文发布");
        } else if (list.get(i).getId_task() == 3) {
            viewHolder.renwu_name.setText("执行任务：朋友圈分享链接");
        } else if (list.get(i).getId_task() == 4) {
            viewHolder.renwu_name.setText("执行任务：搜索加好友");
        } else if (list.get(i).getId_task() == 5) {
            viewHolder.renwu_name.setText("执行任务：通讯录加好友");
        } else if (list.get(i).getId_task() == 6) {
            viewHolder.renwu_name.setText("执行任务：嗅探加好友");
        } else if (list.get(i).getId_task() == 7) {
            viewHolder.renwu_name.setText("执行任务：好友发消息");
        } else if (list.get(i).getId_task() == 8) {
            viewHolder.renwu_name.setText("执行任务：好友发图片");
        } else if (list.get(i).getId_task() == 9) {
            viewHolder.renwu_name.setText("执行任务：好友逐个发消息");
        } else if (list.get(i).getId_task() == 10) {
            viewHolder.renwu_name.setText("执行任务：好友逐个发图片");
        } else if (list.get(i).getId_task() == 11) {
            viewHolder.renwu_name.setText("微信群发消息");
        } else if (list.get(i).getId_task() == 12) {
            viewHolder.renwu_name.setText("执行任务：微信群发图片");
        } else if (list.get(i).getId_task() == 13) {
            viewHolder.renwu_name.setText("执行任务：微信群发名片");
        } else if (list.get(i).getId_task() == 14) {
            viewHolder.renwu_name.setText("执行任务：自动通过好友申请");
        } else if (list.get(i).getId_task() == 15) {
            viewHolder.renwu_name.setText("执行任务：统计好友数量");
        } else if (list.get(i).getId_task() == 16) {
            viewHolder.renwu_name.setText("执行任务：修改个性签名");
        } else if (list.get(i).getId_task() == 17) {
            viewHolder.renwu_name.setText("执行任务：发送公众号名片");
        } else if (list.get(i).getId_task() == 18) {
            viewHolder.renwu_name.setText("执行任务：朋友圈启动游戏");
        } else if (list.get(i).getId_task() == 19) {
            viewHolder.renwu_name.setText("执行任务：朋友圈进入京东购物");
        } else if (list.get(i).getId_task() == 20) {
            viewHolder.renwu_name.setText("执行任务：朋友圈小视频发布");
        } else if (list.get(i).getId_task() == 21) {
            viewHolder.renwu_name.setText("好执行任务：好友发视频");
        } else if (list.get(i).getId_task() == 22) {
            viewHolder.renwu_name.setText("执行任务：微信群发视频");
        } else if (list.get(i).getId_task() == 26) {
            viewHolder.renwu_name.setText("执行任务：加大号添加好友");
        } else if (list.get(i).getId_task() == 25) {
            viewHolder.renwu_name.setText("执行任务：微信统计");
        } else if (list.get(i).getId_task() == 24) {
            viewHolder.renwu_name.setText("执行任务：微信拉群");
        } else if (list.get(i).getId_task() == 27) {
            viewHolder.renwu_name.setText("执行任务：通用设置");
        } else if (list.get(i).getId_task() == 28) {
            viewHolder.renwu_name.setText("执行任务：手机设置");
        } else if (list.get(i).getId_task() == 29) {
            viewHolder.renwu_name.setText("执行任务：自定义修改备注");
        } else if (list.get(i).getId_task() == 30) {
            viewHolder.renwu_name.setText("执行任务：微信群发图文");
        } else if (list.get(i).getId_task() == 31) {
            viewHolder.renwu_name.setText("执行任务：微信养号互聊");
        } else if (list.get(i).getId_task() == 32) {
            viewHolder.renwu_name.setText("执行任务：手机设置初始化");
        } else if (list.get(i).getId_task() == 33) {
            viewHolder.renwu_name.setText("执行任务：手机定时开机和关机");
        } else if (list.get(i).getId_task() == 500) {
            viewHolder.renwu_name.setText("执行任务：支付宝搜索加好友");
        } else if (list.get(i).getId_task() == 501) {
            viewHolder.renwu_name.setText("执行任务：支付宝拉群");
        } else if (list.get(i).getId_task() == 502) {
            viewHolder.renwu_name.setText("执行任务：支付宝好友统计");
        } else if (list.get(i).getId_task() == 503) {
            viewHolder.renwu_name.setText("执行任务：支付宝群好友统计");
        } else if (list.get(i).getId_task() == 504) {
            viewHolder.renwu_name.setText("执行任务：支付推送APK到手机");
        } else if (list.get(i).getId_task() == 505) {
            viewHolder.renwu_name.setText("执行任务：支付宝通用设置");
        } else if (list.get(i).getId_task() == 506) {
            viewHolder.renwu_name.setText("执行任务：支付宝手机设置");
        } else if (list.get(i).getId_task() == 507) {
            viewHolder.renwu_name.setText("执行任务：支付宝初始化任务");
        } else if (list.get(i).getId_task() == 508) {
            viewHolder.renwu_name.setText("执行任务：支付宝设置定时开关机");
        } else if (list.get(i).getId_task() == 509) {
            viewHolder.renwu_name.setText("执行任务：支付宝群图文发布");
        } else if (list.get(i).getId_task() == 34) {
            viewHolder.renwu_name.setText("执行任务：微信语音聊天");
        } else if (list.get(i).getId_task() == 35) {
            viewHolder.renwu_name.setText("执行任务：微信视频聊天");
        } else if (list.get(i).getId_task() == 37) {
            viewHolder.renwu_name.setText("执行任务：微信寻找手机");
        } else if (list.get(i).getId_task() == 510) {
            viewHolder.renwu_name.setText("执行任务：支付宝寻找手机");
        } else if (list.get(i).getId_task() == 511) {
            viewHolder.renwu_name.setText("执行任务：更新版本");
        } else if (list.get(i).getId_task() == 38) {
            viewHolder.renwu_name.setText("浏览公众号");
        } else if (list.get(i).getId_task() == 39) {
            viewHolder.renwu_name.setText("关注公众号");
        } else if (list.get(i).getId_task() == 40) {
            viewHolder.renwu_name.setText("版本更新");
        } else if (list.get(i).getId_task() == 41) {
            viewHolder.renwu_name.setText("执行任务：双向互聊");
        } else if (list.get(i).getId_task() == 42) {
            viewHolder.renwu_name.setText("执行任务：微信发红包");
        } else if (list.get(i).getId_task() == 43) {
            viewHolder.renwu_name.setText("执行任务：统计微信好友信息");
        } else if (list.get(i).getId_task() == 44) {
            viewHolder.renwu_name.setText("执行任务：微信群转发消息到好友");
        } else if (list.get(i).getId_task() == 45) {
            viewHolder.renwu_name.setText("执行任务：微信群转发消息到群");
        } else if (list.get(i).getId_task() == 46) {
            viewHolder.renwu_name.setText("执行任务：二维码拉群");
        } else if (list.get(i).getId_task() == 47) {
            viewHolder.renwu_name.setText("执行任务：自定义拉群");
        } else if (list.get(i).getId_task() == 48) {
            viewHolder.renwu_name.setText("执行任务：阅读信息回复");
        } else if (list.get(i).getId_task() == 69) {
            viewHolder.renwu_name.setText("执行任务：自定义二维码拉群");
        } else if (list.get(i).getId_task() == 497) {
            viewHolder.renwu_name.setText("执行任务：被匹配的语音通话");
        } else if (list.get(i).getId_task() == 498) {
            viewHolder.renwu_name.setText("执行任务：被匹配的视频通话");
        } else if (list.get(i).getId_task() == 499) {
            viewHolder.renwu_name.setText("执行任务：双向互聊");
        } else if (list.get(i).getId_task() == 65) {
            viewHolder.renwu_name.setText("执行任务：语音互聊");
        } else if (list.get(i).getId_task() == 66) {
            viewHolder.renwu_name.setText("执行任务：删除指定朋友圈");
        } else if (list.get(i).getId_task() == 68) {
            viewHolder.renwu_name.setText("执行任务：超级自定义修改备注");
        } else if (list.get(i).getId_task() == 70) {
            viewHolder.renwu_name.setText("执行任务：ZZZ9加大号");
        } else if (list.get(i).getId_task() == 496) {
            viewHolder.renwu_name.setText("执行任务：通过ZZZ9好友申请");
        } else if (list.get(i).getId_task() == 513) {
            viewHolder.renwu_name.setText("执行任务：支付宝群图文发布");
        } else if (list.get(i).getId_task() == 515) {
            viewHolder.renwu_name.setText("执行任务：关注支付宝生活号");
        } else if (list.get(i).getId_task() == 516) {
            viewHolder.renwu_name.setText("执行任务：支付宝生活圈点赞");

        } else if (list.get(i).getId_task() == 517) {
            viewHolder.renwu_name.setText("执行任务：生活圈图文发布");
        } else if (list.get(i).getId_task() == 518) {
            viewHolder.renwu_name.setText("执行任务：生活圈小视频发布");
        } else if (list.get(i).getId_task() == 519) {
            viewHolder.renwu_name.setText("执行任务：支付宝获取名字");
        } else if (list.get(i).getId_task() == 520) {
            viewHolder.renwu_name.setText("执行任务：好友直接群发图文");
        } else if (list.get(i).getId_task() == 521) {
            viewHolder.renwu_name.setText("执行任务：群直接群发图文");
        } else {
            viewHolder.renwu_name.setText("执行任务：未知任务");
        }
        if (list.get(i).getResult().contains("200")) {
            viewHolder.renwu_state.setText("任务执行成功");
        } else if (list.get(i).getResult().contains("400")) {
            viewHolder.renwu_state.setText("任务执行失败");
        } else if (list.get(i).getResult().equals("任务待执行")) {
            viewHolder.renwu_state.setText("任务待执行");
        } else {
            viewHolder.renwu_state.setText("任务正在执行中");
        }


        if ((list.get(i).getResult()).contains("400")) {
            viewHolder.image_state.setImageResource(R.mipmap.shibai);
        } else if (list.get(i).getResult().contains("200")) {
            viewHolder.image_state.setImageResource(R.mipmap.wancheng);
        } else {
            viewHolder.image_state.setImageResource(R.mipmap.wait);
        }
        viewHolder.renwu_time.setText(list.get(i).getTimes() + "");
        viewHolder.log_id.setText("log_id值：" + list.get(i).getLogin_id() + "");
        return view;
    }

    class ViewHolder {
        public TextView renwu_name;//
        public TextView renwu_state;
        public ImageView image_state;//任务状态
        public TextView renwu_time;//任务执行时间
        public TextView log_id;
    }
}
