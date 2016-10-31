package CasinoRoyaleData;

import org.opensplice.dds.dcps.Utilities;

public final class MsgDataReaderHelper
{

    public static CasinoRoyaleData.MsgDataReader narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof CasinoRoyaleData.MsgDataReader) {
            return (CasinoRoyaleData.MsgDataReader)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

    public static CasinoRoyaleData.MsgDataReader unchecked_narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof CasinoRoyaleData.MsgDataReader) {
            return (CasinoRoyaleData.MsgDataReader)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

}
