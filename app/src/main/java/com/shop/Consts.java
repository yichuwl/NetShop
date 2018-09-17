package com.shop;

/**
 * Created by victoryf on 2018-06-28.
 */
public class Consts {
    public static final String WECHAT_APP_ID = ""; //
    public static final String ORDER_TOTAL = "/dashop/order/app/order_total.do"; //  计算订单总价
    public static final String ROOT_URL = "http://39.104.121.196:8082";  //  192.168.0.111、125.41.184.196
    public static final String ADS_FIRST_SHOW = "/dashop/advertise/app/advertisePic";
    public static final String CATEGORY_FIRST_SHOW = "/dashop/category/app/list.do"; // 预留参数?SUPER_ID=0
    public static final String THEME_SEEMORE_GOODS_LIST = "/dashop/goods/app/list.do"; // 商品列表
    public static final String SHEQU_ARTICLE_LIST = "/dashop/news/app/list.do"; // 社区文章列表
    public static final String RECOMMEND_GOODS_LIST = "/dashop/goods/app/list.do?GOODS_HOT=1"; // 为你推荐
    public static final String SIGN_IN = "/dashop/happuser/app/login.do";// 登陆；
    public static final String SIGN_UP = "/dashop/happuser/app/regist.do"; // 注册
    public static final String GET_SINGLE_GOODS = "/dashop/goods/app/getGoods"; //
    public static final String GET_ADDR_LIST = "/dashop/address/app/list"; // 获取地址列表
    public static final String DELETE_ADDR = "/dashop/address/app/delete"; // 删除订单地址
    public static final String EDIT_ADDR = "/dashop/address/app/eidt"; // 修改订单地址
    public static final String SAVE_ADDR = "/dashop/address/app/save"; // 添加地址
    public static final String SAVE_ORDER = "/dashop/order/app/addorder"; // 保存订单
    public static final String CANCEL_ORDER = "/dashop/order/app/editStatus"; // 取消订单
    public static final String GET_CHART_LIST = "/dashop/shoppingcar/app/list.do";  // 获取当前用户的购物车列表
    public static final String REMOVE_CHART_GOODS = "/dashop/shoppingcar/app/delete.do"; // 删除购物车某个商品
    public static final String ADD_CHART_GOODS = "/dashop/shoppingcar/app/add.do"; // 购物车中添加商品
    public static final String GET_GOODS_ATTRIBUTES = "/dashop/attribute/app/list.do"; // 获取商品属性
    public static final String GET_USER_DATA = "/dashop/happuser/app/list.do"; //获取用户信息
    public static final String SAVE_USER_DATA_CHANGE = "/dashop/happuser/app/save.do"; // 提交修改的个人信息
    public static final String GET_GOODS_COMMENT_LIST = "/dashop/goodsrate/app/list.do"; // 获取商品评论列表
    public static final String ADD_GOODS_COMMENT = "/dashop/goodsrate/app/save.do"; // 新增商品评论
    public static final String GET_TIME_TO_BY = "/dashop/seckill/app/getSecKill.do"; //获取秒杀信息
    public static final String GET_TIME_TO_BY_GOODS_LIST = "/dashop/seckillgoods/app/getSecKillGoods.do"; // 获取秒杀商品列表
    public static final String ADD_COMMENT_NEWS = "/dashop/newsrate/app/add.do"; // 添加新闻评论
    public static final String GET_COMMENT_NEWS = "/dashop/newsrate/app/list.do"; // 获取新闻评论列表
    public static final String GET_USER_COUPON_LIST = "/dashop/usercoupon/app/list.do"; // 获取会员优惠券列表
    public static final String GET_COUPON_LIST = "/dashop/coupon/app/list.do"; // 获取活动优惠券列表
    public static final String SAVE_COUPON = "/dashop/usercoupon/app/save.do"; // 领用优惠券
    public static final String GET_ORDER_LIST = "/dashop/order/app/list.do"; // 获取订单列表
    public static final String ALIPAY_URL = "/dashop/alipay/app/alipaybiz.do";
    // 参数
    public static final String TOPAYSTATE = "0";
    public static final String TOSENDSTATE = "1";
    public static final String TORECEIVESTATE = "2";
    public static final String TOCOMMENTSTATE = "7";

    public static final String ADS_PARMA_KEY = "ADV_FLAG";
    public static final String BANNER_PARAM = "A1";
    public static final String TIME_PARAM = "A2";
    public static final String FENLEI1_PARAM = "FL1";
    public static final String FENLEI2_PARAM = "FL2";
    public static final String FENLEI3_PARAM = "FL3";
    public static final String FENLEI4_PARAM = "FL4";
    public static final String FENLEI5_PARAM = "FL5";
    public static final String FENLEI6_PARAM = "FL6";
    public static final String FENLEI_PARAM = "FL";

    public static final String RESPONSE_OK_STATE = "OK";
    public static final String PARAM_GOODS_LIST_THEME_FIRST = "SUPER_CAT";
    public static final String PARAM_GOODS_LIST_PARAM = "params";

    // order state flag-------------------------------------
    public static final int TO_BE_PAY_ORDER = 0;
    public static final int TO_BE_SEND_ORDER = 1;
    public static final int TO_BE_RECEIVE_ORDER = 2;
    public static final int TO_BE_COMMENT = 5;
    public static final int TO_PAY_BACK = 3;
    public static final int PAY_BACK = 4;
    public static final int TRADE_SUCESS = 6;
    public static final int TRADE_CLOSE = 99;

    //public static final int
}
