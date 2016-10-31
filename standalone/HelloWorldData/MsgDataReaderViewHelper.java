package CasinoRoyaleData;

import org.opensplice.dds.dcps.Utilities;

public final class MsgDataReaderViewHelper
{

    public static CasinoRoyaleData.MsgDataReaderView narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof CasinoRoyaleData.MsgDataReaderView) {
            return (CasinoRoyaleData.MsgDataReaderView)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

    public static CasinoRoyaleData.MsgDataReaderView unchecked_narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof CasinoRoyaleData.MsgDataReaderView) {
            return (CasinoRoyaleData.MsgDataReaderView)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

}
