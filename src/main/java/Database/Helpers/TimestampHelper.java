package Database.Helpers;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimestampHelper {

    public static Timestamp toTimestamp(String time){
        String str = ZonedDateTime.parse(time).format(DateTimeFormatter.ISO_INSTANT);
        return  new Timestamp(ZonedDateTime.parse(str).toLocalDateTime().toInstant(ZoneOffset.UTC).toEpochMilli());
    }

    public static String fromTimestamp(Timestamp timestamp){
        return timestamp.toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

}
