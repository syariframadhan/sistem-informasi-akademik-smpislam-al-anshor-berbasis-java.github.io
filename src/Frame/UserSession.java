package Frame;

class UserSession {
     private static String userLogin;

    public static void setUserLogin(String userLogin) {
        UserSession.userLogin = userLogin;
    }
    
    
    
    
    
    
    public static String getUserLogin() {
        return userLogin;
    }
}
