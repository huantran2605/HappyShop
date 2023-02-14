package com.happyshop;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import com.happyshop.security.oauth2.CustomerOauth2User;
import com.happyshop.setting.CurrencySettingBag;
import com.happyshop.setting.EmailSettingBag;

public class Utility {
   
    
  public static String getSiteUrl(HttpServletRequest request) {
    String url = request.getRequestURL().toString();
    return url.replace(request.getServletPath(), "");
  }
  
  public static JavaMailSenderImpl prepareMailSender(EmailSettingBag settings) {
      JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
      
      mailSender.setHost(settings.getHost());
      mailSender.setPort(settings.getPort());
      mailSender.setUsername(settings.getUsername());
      mailSender.setPassword(settings.getPassword());

      Properties mailProperties = new Properties();
      mailProperties.setProperty("mail.smtp.auth", settings.getSmtpAuth());
      mailProperties.setProperty("mail.smtp.starttls.enable", settings.getSmtpSecured());
      mailSender.setJavaMailProperties(mailProperties);
      
      return mailSender;
  
  }
  
  public static String getEmailAuthenticationCustomer(HttpServletRequest request) {
      Object principle =  request.getUserPrincipal();
      if(principle == null ) {
          return null;
      }
      String email = "";
      if(principle instanceof UsernamePasswordAuthenticationToken
              || principle instanceof RememberMeAuthenticationToken) {
          email = request.getUserPrincipal().getName();
      }
      else if(principle instanceof OAuth2AuthenticationToken){
          OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) principle;
          CustomerOauth2User oauth2User = (CustomerOauth2User) oauth2Token.getPrincipal();
          email =  oauth2User.getEmail();
      }       
      return email;
  }
  
  public static String formatCurrency(float amount, CurrencySettingBag settings) {
      String symbol = settings.getSymbol();
      String symbolPosition = settings.getSymbolPosition();
      String decimalPointType = settings.getDecimalPointType();
      String thousandPointType = settings.getThousandPointType();
      int decimalDigits = settings.getDecimalDigits();
      
      String pattern = symbolPosition.equals("Before price") ? symbol : "";
      pattern += "###,###";
      
      if (decimalDigits > 0) {
          pattern += ".";
          for (int count = 1; count <= decimalDigits; count++) pattern += "#";
      }
      
      pattern += symbolPosition.equals("After price") ? symbol : "";
      
      char thousandSeparator = thousandPointType.equals("POINT") ? '.' : ',';
      char decimalSeparator = decimalPointType.equals("POINT") ? '.' : ',';
      
      DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
      decimalFormatSymbols.setDecimalSeparator(decimalSeparator);
      decimalFormatSymbols.setGroupingSeparator(thousandSeparator);
      
      DecimalFormat formatter = new DecimalFormat(pattern, decimalFormatSymbols);
      
      return formatter.format(amount);
  }
  

}
