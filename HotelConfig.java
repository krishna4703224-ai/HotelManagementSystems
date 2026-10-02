package hotel;

public class HotelConfig {
    private static String hotelName = "ROYAL STAY";
    private static String hotelAddress = "123 Luxury Avenue, City Center";
    private static String hotelPhone = "+1 (555) 019-2834";

    public static String getHotelName() {
        return hotelName;
    }

    public static void setHotelName(String name) {
        hotelName = name;
    }

    public static String getHotelAddress() {
        return hotelAddress;
    }

    public static void setHotelAddress(String address) {
        hotelAddress = address;
    }

    public static String getHotelPhone() {
        return hotelPhone;
    }

    public static void setHotelPhone(String phone) {
        hotelPhone = phone;
    }
}