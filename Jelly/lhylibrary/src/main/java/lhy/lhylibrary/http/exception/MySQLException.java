package lhy.lhylibrary.http.exception;

import java.sql.SQLException;

/**
 * Created by Liheyu on 2017/3/22.
 * Email:liheyu999@163.com
 */

public class MySQLException extends SQLException {

    public MySQLException(String theReason) {
        super(theReason);
    }
}
