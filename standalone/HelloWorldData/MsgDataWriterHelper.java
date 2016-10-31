package CasinoRoyaleData;

import org.opensplice.dds.dcps.Utilities;

public final class MsgDataWriterHelper
{

    public static CasinoRoyaleData.MsgDataWriter narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof CasinoRoyaleData.MsgDataWriter) {
            return (CasinoRoyaleData.MsgDataWriter)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

    public static CasinoRoyaleData.MsgDataWriter unchecked_narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof CasinoRoyaleData.MsgDataWriter) {
            return (CasinoRoyaleData.MsgDataWriter)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

}
