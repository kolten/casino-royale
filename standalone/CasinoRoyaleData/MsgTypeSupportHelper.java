package CasinoRoyaleData;

import org.opensplice.dds.dcps.Utilities;

public final class MsgTypeSupportHelper
{

    public static CasinoRoyaleData.MsgTypeSupport narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof CasinoRoyaleData.MsgTypeSupport) {
            return (CasinoRoyaleData.MsgTypeSupport)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

    public static CasinoRoyaleData.MsgTypeSupport unchecked_narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof CasinoRoyaleData.MsgTypeSupport) {
            return (CasinoRoyaleData.MsgTypeSupport)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

}
