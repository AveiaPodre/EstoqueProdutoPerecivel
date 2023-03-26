package AverinaldoJunior.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private static Connection con = null;

    public static Connection getCon() throws DAOException {
        if (con == null) {
            try {
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/estoque", "root", "Jr11062004#");
            } catch (SQLException e) {
                throw new DAOException(e.getMessage());
            }
        }
        return con;
    }

    public void closeCon() throws DAOException {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                throw new DAOException(e.getMessage());
            }
        }
    }
}