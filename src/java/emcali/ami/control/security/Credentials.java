/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emcali.ami.control.security;

/**
 *
 * @author julian
 */
public class Credentials {
    private final String login = "telcoiptv";
    private final String password = "amiproject";
    
    public String authenticate(String login, String password){
        if(login.equals(this.login)){
            if(password.equals(this.password)){
                return "success";
            } else {
                return "wrong password";
            }
        } else{
            return "user unauthorized";
        }
    }
}
