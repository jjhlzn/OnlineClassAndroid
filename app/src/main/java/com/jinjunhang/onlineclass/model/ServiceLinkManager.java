package com.jinjunhang.onlineclass.model;

import com.jinjunhang.onlineclass.service.ServiceConfiguration;

/**
 * Created by jjh on 2016-6-29.
 */
public class ServiceLinkManager {


    public static String  MyTuiJianUrl() {
        return "http://"+ ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/Center/MyTuiJian";
    }

    public static String  MyOrderUrl() {
        return "http://"+ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/Center/MyOrder";

    }

    public static String  MyAgentUrl() {
        return  "http://"+ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/Center/MyAgent";

    }

    public static String  MyAgentUrl2() {
        return  "http://"+ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/Center/MyLevel2.aspx";

    }

    public static String MyExchangeUrl() {

        return "http://"+ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/Center/MyExchange";

    }


    public static String FunctionCardManagerUrl() {

        return "http://"+ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/Service/CardManage";

    }

    public static String FunctionCustomerServiceUrl() {

        return "http://"+ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/Service/Custom";

    }


    public static String FunctionUpUrl() {

        return "http://"+ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/Service/CreditLines";

    }

    public static String FunctionFastCardUrl() {

        return "http://"+ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/Service/FastCard";

    }

    public static String FunctionCreditSearchUrl() {

        return "http://"+ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/Service/Ipcrs";

    }


    public static String FunctionMccSearchUrl() {
        return "http://"+ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/Service/MccSearch";
    }

    public static String FunctionJiaoFeiUrl() {
        return "http://"+ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/Service/Fee";
    }

    public static String FunctionLoanUrl() {
        return "http://"+ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/Service/Loan";
    }

    public static String FunctionCarLoanUrl() {
        return "http://"+ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/Service/CarLoan";
    }

    public static String FunctionShopUrl() {
        return "http://"+ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/shop/shopindex";
    }


    public static String ShareQrImageUrl() {
        return "http://jf.yhkamani.com/Center/MyLink.aspx";
    }

    public static String AgreementUrl() {
        return "http://"+ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/agreement.html";
    }

    public static String MyjifenUrl() {
        return  "http://"+ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/Center/MyPoint";
    }

    public static String MyChaifuUrl() {
        return  "http://"+ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/Center/MyMoney";
    }

    public static String MyTeamUrl() {
        return  "http://"+ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/Center/MyTeam";
    }

    public static String MyTeamUrl2() {
        return  "http://"+ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/Center/MyTeam2";
    }

    public static String PersonalInfoUrl() {
        return  "http://"+ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/Center/PersonalInfo";
    }

    public static String ChatServerUrl() {
        return "http://chat.yhkamani.com";
        //return "http://192.168.1.102:3000";
    }

    public static String ShenqingUrl() {
        return  "http://"+ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/app/shenqing";
    }

    public static String DefaultAdvImageUrl() {
        return  "http://"+ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/images/liveSampleImage.png";
    }

    public static String StudyUrl() {
        return  "http://"+ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/app/study";
    }

    public static String ZhuanLanUrl() {
        return  "http://"+ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/app/getZhuanLans";
    }

    public static String JunHuoKuUrl() {
        return  "http://"+ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/app/documents";
    }

    public static String FinaceToolsUrl() {
        return  "http://"+ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/app/financetools";
    }

    public static String MallUrl() {
        return  "http://"+ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/app/mall";
    }

    public static String HezuoUrl() {
        return  "http://"+ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/app/hezuo";
    }

    public static String MyServiceUrl() {
        return  "http://"+ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/app/myservice";
    }

    public static String CardPayUrl() {
        return  "http://"+ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/app/pay";
    }

    public static String HealthUrl() {
        return  "http://"+ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/app/health";
    }

    public static String QiandaoUrl() {
        return  "http://"+ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/app/signin";
    }

    public static String YigouUrl() {
        return  "http://"+ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/app/purchased";
    }

    public static String MyWalletUrl() {
        return  "http://"+ServiceConfiguration.serverName()+":"+ServiceConfiguration.port()+"/app/mywallet";
    }
}

