package com.jinjunhang.onlineclass.model;

/**
 * Created by jjh on 2016-6-29.
 */
public class ServiceLinkManager {
    private static String host = "114.215.236.171";
    private static String port = "6012";


    public static String  MyTuiJianUrl() {
        return "http://"+host+":"+port+"/Center/MyTuiJian";
    }

    public static String  MyOrderUrl() {
        return "http://"+host+":"+port+"/Center/MyOrder";

    }

    public static String  MyAgentUrl() {
        return  "http://"+host+":"+port+"/Center/MyAgent";

    }

    public static String MyExchangeUrl() {

        return "http://"+host+":"+port+"/Center/MyExchange";

    }

    public static String MyTeamUrl() {

        return "http://"+host+":"+port+"/Center/MyTeam";

    }

    public static String FunctionCardManagerUrl() {

        return "http://"+host+":"+port+"/Service/CardManage";

    }

    public static String FunctionCustomerServiceUrl() {

        return "http://"+host+":"+port+"/Service/Custom";

    }


    public static String FunctionUpUrl() {

        return "http://"+host+":"+port+"/Service/CreditLines";

    }

    public static String FunctionFastCardUrl() {

        return "http://"+host+":"+port+"/Service/FastCard";

    }

    public static String FunctionCreditSearchUrl() {

        return "http://"+host+":"+port+"/Service/Ipcrs";

    }


    public static String FunctionMccSearchUrl() {
        return "http://"+host+":"+port+"/Service/MccSearch";
    }

    public static String FunctionJiaoFeiUrl() {
        return "http://"+host+":"+port+"/Service/Fee";
    }

    public static String FunctionLoanUrl() {
        return "http://"+host+":"+port+"/Service/Loan";
    }

    public static String FunctionCarLoanUrl() {
        return "http://"+host+":"+port+"/Service/CarLoan";
    }

    public static String FunctionShopUrl() {
        return "http://"+host+":"+port+"/shop/shopindex";
    }


    public static String ShareQrImageUrl() {
        return "http://"+host+":"+port+"/Center/MyLink.aspx";
    }

}

