package primedsoft.com.salvation.Wsdl2Code.WebServices.EnterpriseAPISoapService;

//------------------------------------------------------------------------------
// <wsdl2code-generated>
//    This code was generated by http://www.wsdl2code.com version  2.5
//
// Date Of Creation: 7/4/2014 4:34:19 PM
//    Please dont change this code, regeneration will override your changes
//</wsdl2code-generated>
//
//------------------------------------------------------------------------------
//
//This source code was auto-generated by Wsdl2Code  Version
//

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.util.Hashtable;

public class bankTransfer implements KvmSerializable {

    public String accountHolder;
    public String accountNumber;
    public String accountType;
    public String actionDate;
    public String amountInCents;
    public String branchCode;
    public String clearanceDate;
    public String defaultPM;
    public String pmId;

    public bankTransfer() {
    }

    public bankTransfer(SoapObject soapObject) {
        if (soapObject == null)
            return;
        if (soapObject.hasProperty("accountHolder")) {
            Object obj = soapObject.getProperty("accountHolder");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                accountHolder = j.toString();
            } else if (obj != null && obj instanceof String) {
                accountHolder = (String) obj;
            }
        }
        if (soapObject.hasProperty("accountNumber")) {
            Object obj = soapObject.getProperty("accountNumber");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                accountNumber = j.toString();
            } else if (obj != null && obj instanceof String) {
                accountNumber = (String) obj;
            }
        }
        if (soapObject.hasProperty("accountType")) {
            Object obj = soapObject.getProperty("accountType");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                accountType = j.toString();
            } else if (obj != null && obj instanceof String) {
                accountType = (String) obj;
            }
        }
        if (soapObject.hasProperty("actionDate")) {
            Object obj = soapObject.getProperty("actionDate");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                actionDate = j.toString();
            } else if (obj != null && obj instanceof String) {
                actionDate = (String) obj;
            }
        }
        if (soapObject.hasProperty("amountInCents")) {
            Object obj = soapObject.getProperty("amountInCents");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                amountInCents = j.toString();
            } else if (obj != null && obj instanceof String) {
                amountInCents = (String) obj;
            }
        }
        if (soapObject.hasProperty("branchCode")) {
            Object obj = soapObject.getProperty("branchCode");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                branchCode = j.toString();
            } else if (obj != null && obj instanceof String) {
                branchCode = (String) obj;
            }
        }
        if (soapObject.hasProperty("clearanceDate")) {
            Object obj = soapObject.getProperty("clearanceDate");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                clearanceDate = j.toString();
            } else if (obj != null && obj instanceof String) {
                clearanceDate = (String) obj;
            }
        }
        if (soapObject.hasProperty("defaultPM")) {
            Object obj = soapObject.getProperty("defaultPM");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                defaultPM = j.toString();
            } else if (obj != null && obj instanceof String) {
                defaultPM = (String) obj;
            }
        }
        if (soapObject.hasProperty("pmId")) {
            Object obj = soapObject.getProperty("pmId");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                pmId = j.toString();
            } else if (obj != null && obj instanceof String) {
                pmId = (String) obj;
            }
        }
    }

    @Override
    public Object getProperty(int arg0) {
        switch (arg0) {
            case 0:
                return accountHolder;
            case 1:
                return accountNumber;
            case 2:
                return accountType;
            case 3:
                return actionDate;
            case 4:
                return amountInCents;
            case 5:
                return branchCode;
            case 6:
                return clearanceDate;
            case 7:
                return defaultPM;
            case 8:
                return pmId;
        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 9;
    }

    @Override
    public void getPropertyInfo(int index, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info) {
        switch (index) {
            case 0:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "accountHolder";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "accountNumber";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "accountType";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "actionDate";
                break;
            case 4:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "amountInCents";
                break;
            case 5:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "branchCode";
                break;
            case 6:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "clearanceDate";
                break;
            case 7:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "defaultPM";
                break;
            case 8:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "pmId";
                break;
        }
    }

    @Override
    public void setProperty(int arg0, Object arg1) {
    }

}
