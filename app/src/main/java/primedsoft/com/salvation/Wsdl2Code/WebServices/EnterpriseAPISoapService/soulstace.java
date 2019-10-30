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

public class soulstace implements KvmSerializable {

    public String amountInCents;
    public String basketAmountInCents;
    public String basketCurrency;
    public String basketDescription;
    public String customerContactNumber;
    public String customerEmail;
    public String customerFirstName;
    public String customerId;
    public String customerLastName;
    public String customerTrxRef;
    public String ibsLoggerUpdateReferenceId;
    public String mcaCustomerCareGetProductsResultCode;
    public String mcaCustomerCareGetProductsResultMsg;
    public String mcaLoggerUpdateResultCode;
    public String mcaLoggerUpdateResultMsg;
    public String mcaRtppPaymentResultCode;
    public String mcaRtppPaymentResultMsg;
    public String paymentChannel;
    public String payuMerchantId;
    public String selectedEwalletForPayment;
    public String soulsticeISwitchSubQueryBankCount;
    public String soulsticeISwitchSubQueryResultCode;
    public String soulsticeISwitchSubQueryResultMsg;
    public String soulsticePaymentTransactionId;
    public String soulsticeSubmitPaymentResultCode;
    public String soulsticeSubmitPaymentResultMsg;
    public String soulsticeUmmDataHolderResponseMsg;
    public String soulsticeUmmDataHolderResultCode;
    public String transactionDate;
    public String transactionReference;
    public String transactionStatus;
    public String transactionStatusDescription;
    public String transactionType;
    public String defaultPM;
    public String pmId;

    public soulstace() {
    }

    public soulstace(SoapObject soapObject) {
        if (soapObject == null)
            return;
        if (soapObject.hasProperty("amountInCents")) {
            Object obj = soapObject.getProperty("amountInCents");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                amountInCents = j.toString();
            } else if (obj != null && obj instanceof String) {
                amountInCents = (String) obj;
            }
        }
        if (soapObject.hasProperty("basketAmountInCents")) {
            Object obj = soapObject.getProperty("basketAmountInCents");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                basketAmountInCents = j.toString();
            } else if (obj != null && obj instanceof String) {
                basketAmountInCents = (String) obj;
            }
        }
        if (soapObject.hasProperty("basketCurrency")) {
            Object obj = soapObject.getProperty("basketCurrency");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                basketCurrency = j.toString();
            } else if (obj != null && obj instanceof String) {
                basketCurrency = (String) obj;
            }
        }
        if (soapObject.hasProperty("basketDescription")) {
            Object obj = soapObject.getProperty("basketDescription");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                basketDescription = j.toString();
            } else if (obj != null && obj instanceof String) {
                basketDescription = (String) obj;
            }
        }
        if (soapObject.hasProperty("customerContactNumber")) {
            Object obj = soapObject.getProperty("customerContactNumber");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                customerContactNumber = j.toString();
            } else if (obj != null && obj instanceof String) {
                customerContactNumber = (String) obj;
            }
        }
        if (soapObject.hasProperty("customerEmail")) {
            Object obj = soapObject.getProperty("customerEmail");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                customerEmail = j.toString();
            } else if (obj != null && obj instanceof String) {
                customerEmail = (String) obj;
            }
        }
        if (soapObject.hasProperty("customerFirstName")) {
            Object obj = soapObject.getProperty("customerFirstName");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                customerFirstName = j.toString();
            } else if (obj != null && obj instanceof String) {
                customerFirstName = (String) obj;
            }
        }
        if (soapObject.hasProperty("customerId")) {
            Object obj = soapObject.getProperty("customerId");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                customerId = j.toString();
            } else if (obj != null && obj instanceof String) {
                customerId = (String) obj;
            }
        }
        if (soapObject.hasProperty("customerLastName")) {
            Object obj = soapObject.getProperty("customerLastName");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                customerLastName = j.toString();
            } else if (obj != null && obj instanceof String) {
                customerLastName = (String) obj;
            }
        }
        if (soapObject.hasProperty("customerTrxRef")) {
            Object obj = soapObject.getProperty("customerTrxRef");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                customerTrxRef = j.toString();
            } else if (obj != null && obj instanceof String) {
                customerTrxRef = (String) obj;
            }
        }
        if (soapObject.hasProperty("ibsLoggerUpdateReferenceId")) {
            Object obj = soapObject.getProperty("ibsLoggerUpdateReferenceId");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                ibsLoggerUpdateReferenceId = j.toString();
            } else if (obj != null && obj instanceof String) {
                ibsLoggerUpdateReferenceId = (String) obj;
            }
        }
        if (soapObject.hasProperty("mcaCustomerCareGetProductsResultCode")) {
            Object obj = soapObject.getProperty("mcaCustomerCareGetProductsResultCode");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                mcaCustomerCareGetProductsResultCode = j.toString();
            } else if (obj != null && obj instanceof String) {
                mcaCustomerCareGetProductsResultCode = (String) obj;
            }
        }
        if (soapObject.hasProperty("mcaCustomerCareGetProductsResultMsg")) {
            Object obj = soapObject.getProperty("mcaCustomerCareGetProductsResultMsg");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                mcaCustomerCareGetProductsResultMsg = j.toString();
            } else if (obj != null && obj instanceof String) {
                mcaCustomerCareGetProductsResultMsg = (String) obj;
            }
        }
        if (soapObject.hasProperty("mcaLoggerUpdateResultCode")) {
            Object obj = soapObject.getProperty("mcaLoggerUpdateResultCode");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                mcaLoggerUpdateResultCode = j.toString();
            } else if (obj != null && obj instanceof String) {
                mcaLoggerUpdateResultCode = (String) obj;
            }
        }
        if (soapObject.hasProperty("mcaLoggerUpdateResultMsg")) {
            Object obj = soapObject.getProperty("mcaLoggerUpdateResultMsg");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                mcaLoggerUpdateResultMsg = j.toString();
            } else if (obj != null && obj instanceof String) {
                mcaLoggerUpdateResultMsg = (String) obj;
            }
        }
        if (soapObject.hasProperty("mcaRtppPaymentResultCode")) {
            Object obj = soapObject.getProperty("mcaRtppPaymentResultCode");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                mcaRtppPaymentResultCode = j.toString();
            } else if (obj != null && obj instanceof String) {
                mcaRtppPaymentResultCode = (String) obj;
            }
        }
        if (soapObject.hasProperty("mcaRtppPaymentResultMsg")) {
            Object obj = soapObject.getProperty("mcaRtppPaymentResultMsg");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                mcaRtppPaymentResultMsg = j.toString();
            } else if (obj != null && obj instanceof String) {
                mcaRtppPaymentResultMsg = (String) obj;
            }
        }
        if (soapObject.hasProperty("paymentChannel")) {
            Object obj = soapObject.getProperty("paymentChannel");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                paymentChannel = j.toString();
            } else if (obj != null && obj instanceof String) {
                paymentChannel = (String) obj;
            }
        }
        if (soapObject.hasProperty("payuMerchantId")) {
            Object obj = soapObject.getProperty("payuMerchantId");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                payuMerchantId = j.toString();
            } else if (obj != null && obj instanceof String) {
                payuMerchantId = (String) obj;
            }
        }
        if (soapObject.hasProperty("selectedEwalletForPayment")) {
            Object obj = soapObject.getProperty("selectedEwalletForPayment");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                selectedEwalletForPayment = j.toString();
            } else if (obj != null && obj instanceof String) {
                selectedEwalletForPayment = (String) obj;
            }
        }
        if (soapObject.hasProperty("soulsticeISwitchSubQueryBankCount")) {
            Object obj = soapObject.getProperty("soulsticeISwitchSubQueryBankCount");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                soulsticeISwitchSubQueryBankCount = j.toString();
            } else if (obj != null && obj instanceof String) {
                soulsticeISwitchSubQueryBankCount = (String) obj;
            }
        }
        if (soapObject.hasProperty("soulsticeISwitchSubQueryResultCode")) {
            Object obj = soapObject.getProperty("soulsticeISwitchSubQueryResultCode");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                soulsticeISwitchSubQueryResultCode = j.toString();
            } else if (obj != null && obj instanceof String) {
                soulsticeISwitchSubQueryResultCode = (String) obj;
            }
        }
        if (soapObject.hasProperty("soulsticeISwitchSubQueryResultMsg")) {
            Object obj = soapObject.getProperty("soulsticeISwitchSubQueryResultMsg");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                soulsticeISwitchSubQueryResultMsg = j.toString();
            } else if (obj != null && obj instanceof String) {
                soulsticeISwitchSubQueryResultMsg = (String) obj;
            }
        }
        if (soapObject.hasProperty("soulsticePaymentTransactionId")) {
            Object obj = soapObject.getProperty("soulsticePaymentTransactionId");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                soulsticePaymentTransactionId = j.toString();
            } else if (obj != null && obj instanceof String) {
                soulsticePaymentTransactionId = (String) obj;
            }
        }
        if (soapObject.hasProperty("soulsticeSubmitPaymentResultCode")) {
            Object obj = soapObject.getProperty("soulsticeSubmitPaymentResultCode");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                soulsticeSubmitPaymentResultCode = j.toString();
            } else if (obj != null && obj instanceof String) {
                soulsticeSubmitPaymentResultCode = (String) obj;
            }
        }
        if (soapObject.hasProperty("soulsticeSubmitPaymentResultMsg")) {
            Object obj = soapObject.getProperty("soulsticeSubmitPaymentResultMsg");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                soulsticeSubmitPaymentResultMsg = j.toString();
            } else if (obj != null && obj instanceof String) {
                soulsticeSubmitPaymentResultMsg = (String) obj;
            }
        }
        if (soapObject.hasProperty("soulsticeUmmDataHolderResponseMsg")) {
            Object obj = soapObject.getProperty("soulsticeUmmDataHolderResponseMsg");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                soulsticeUmmDataHolderResponseMsg = j.toString();
            } else if (obj != null && obj instanceof String) {
                soulsticeUmmDataHolderResponseMsg = (String) obj;
            }
        }
        if (soapObject.hasProperty("soulsticeUmmDataHolderResultCode")) {
            Object obj = soapObject.getProperty("soulsticeUmmDataHolderResultCode");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                soulsticeUmmDataHolderResultCode = j.toString();
            } else if (obj != null && obj instanceof String) {
                soulsticeUmmDataHolderResultCode = (String) obj;
            }
        }
        if (soapObject.hasProperty("transactionDate")) {
            Object obj = soapObject.getProperty("transactionDate");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                transactionDate = j.toString();
            } else if (obj != null && obj instanceof String) {
                transactionDate = (String) obj;
            }
        }
        if (soapObject.hasProperty("transactionReference")) {
            Object obj = soapObject.getProperty("transactionReference");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                transactionReference = j.toString();
            } else if (obj != null && obj instanceof String) {
                transactionReference = (String) obj;
            }
        }
        if (soapObject.hasProperty("transactionStatus")) {
            Object obj = soapObject.getProperty("transactionStatus");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                transactionStatus = j.toString();
            } else if (obj != null && obj instanceof String) {
                transactionStatus = (String) obj;
            }
        }
        if (soapObject.hasProperty("transactionStatusDescription")) {
            Object obj = soapObject.getProperty("transactionStatusDescription");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                transactionStatusDescription = j.toString();
            } else if (obj != null && obj instanceof String) {
                transactionStatusDescription = (String) obj;
            }
        }
        if (soapObject.hasProperty("transactionType")) {
            Object obj = soapObject.getProperty("transactionType");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
                SoapPrimitive j = (SoapPrimitive) obj;
                transactionType = j.toString();
            } else if (obj != null && obj instanceof String) {
                transactionType = (String) obj;
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
                return amountInCents;
            case 1:
                return basketAmountInCents;
            case 2:
                return basketCurrency;
            case 3:
                return basketDescription;
            case 4:
                return customerContactNumber;
            case 5:
                return customerEmail;
            case 6:
                return customerFirstName;
            case 7:
                return customerId;
            case 8:
                return customerLastName;
            case 9:
                return customerTrxRef;
            case 10:
                return ibsLoggerUpdateReferenceId;
            case 11:
                return mcaCustomerCareGetProductsResultCode;
            case 12:
                return mcaCustomerCareGetProductsResultMsg;
            case 13:
                return mcaLoggerUpdateResultCode;
            case 14:
                return mcaLoggerUpdateResultMsg;
            case 15:
                return mcaRtppPaymentResultCode;
            case 16:
                return mcaRtppPaymentResultMsg;
            case 17:
                return paymentChannel;
            case 18:
                return payuMerchantId;
            case 19:
                return selectedEwalletForPayment;
            case 20:
                return soulsticeISwitchSubQueryBankCount;
            case 21:
                return soulsticeISwitchSubQueryResultCode;
            case 22:
                return soulsticeISwitchSubQueryResultMsg;
            case 23:
                return soulsticePaymentTransactionId;
            case 24:
                return soulsticeSubmitPaymentResultCode;
            case 25:
                return soulsticeSubmitPaymentResultMsg;
            case 26:
                return soulsticeUmmDataHolderResponseMsg;
            case 27:
                return soulsticeUmmDataHolderResultCode;
            case 28:
                return transactionDate;
            case 29:
                return transactionReference;
            case 30:
                return transactionStatus;
            case 31:
                return transactionStatusDescription;
            case 32:
                return transactionType;
            case 33:
                return defaultPM;
            case 34:
                return pmId;
        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 35;
    }

    @Override
    public void getPropertyInfo(int index, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info) {
        switch (index) {
            case 0:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "amountInCents";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "basketAmountInCents";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "basketCurrency";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "basketDescription";
                break;
            case 4:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "customerContactNumber";
                break;
            case 5:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "customerEmail";
                break;
            case 6:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "customerFirstName";
                break;
            case 7:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "customerId";
                break;
            case 8:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "customerLastName";
                break;
            case 9:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "customerTrxRef";
                break;
            case 10:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "ibsLoggerUpdateReferenceId";
                break;
            case 11:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "mcaCustomerCareGetProductsResultCode";
                break;
            case 12:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "mcaCustomerCareGetProductsResultMsg";
                break;
            case 13:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "mcaLoggerUpdateResultCode";
                break;
            case 14:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "mcaLoggerUpdateResultMsg";
                break;
            case 15:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "mcaRtppPaymentResultCode";
                break;
            case 16:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "mcaRtppPaymentResultMsg";
                break;
            case 17:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "paymentChannel";
                break;
            case 18:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "payuMerchantId";
                break;
            case 19:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "selectedEwalletForPayment";
                break;
            case 20:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "soulsticeISwitchSubQueryBankCount";
                break;
            case 21:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "soulsticeISwitchSubQueryResultCode";
                break;
            case 22:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "soulsticeISwitchSubQueryResultMsg";
                break;
            case 23:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "soulsticePaymentTransactionId";
                break;
            case 24:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "soulsticeSubmitPaymentResultCode";
                break;
            case 25:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "soulsticeSubmitPaymentResultMsg";
                break;
            case 26:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "soulsticeUmmDataHolderResponseMsg";
                break;
            case 27:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "soulsticeUmmDataHolderResultCode";
                break;
            case 28:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "transactionDate";
                break;
            case 29:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "transactionReference";
                break;
            case 30:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "transactionStatus";
                break;
            case 31:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "transactionStatusDescription";
                break;
            case 32:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "transactionType";
                break;
            case 33:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "defaultPM";
                break;
            case 34:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "pmId";
                break;
        }
    }

    @Override
    public void setProperty(int arg0, Object arg1) {
    }

}
