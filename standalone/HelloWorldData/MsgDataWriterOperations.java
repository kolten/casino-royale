package CasinoRoyaleData;

public interface MsgDataWriterOperations extends
    DDS.DataWriterOperations
{

    long register_instance(
            CasinoRoyaleData.Msg instance_data);

    long register_instance_w_timestamp(
            CasinoRoyaleData.Msg instance_data, 
            DDS.Time_t source_timestamp);

    int unregister_instance(
            CasinoRoyaleData.Msg instance_data, 
            long handle);

    int unregister_instance_w_timestamp(
            CasinoRoyaleData.Msg instance_data, 
            long handle, 
            DDS.Time_t source_timestamp);

    int write(
            CasinoRoyaleData.Msg instance_data, 
            long handle);

    int write_w_timestamp(
            CasinoRoyaleData.Msg instance_data, 
            long handle, 
            DDS.Time_t source_timestamp);

    int dispose(
            CasinoRoyaleData.Msg instance_data, 
            long instance_handle);

    int dispose_w_timestamp(
            CasinoRoyaleData.Msg instance_data, 
            long instance_handle, 
            DDS.Time_t source_timestamp);
    
    int writedispose(
            CasinoRoyaleData.Msg instance_data, 
            long instance_handle);

    int writedispose_w_timestamp(
            CasinoRoyaleData.Msg instance_data, 
            long instance_handle, 
            DDS.Time_t source_timestamp);

    int get_key_value(
            CasinoRoyaleData.MsgHolder key_holder, 
            long handle);
    
    long lookup_instance(
            CasinoRoyaleData.Msg instance_data);

}
