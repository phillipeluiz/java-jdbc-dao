package model.dao.impl;

import com.mysql.cj.protocol.Resultset;
import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class SellerDaoJDBC  implements SellerDao {

    private Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller department) {

    }

    @Override
    public void update(Seller department) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {

            st = conn.prepareStatement( " select seller.*, department.Name as DepName " +
                    " From seller INNER JOIN department ON seller.DepartmentId = department.Id " +
                    " Where seller.Id = ?");

            st.setInt(1,id);

            rs = st.executeQuery();

            if (rs.next()) {
                Department dep = new Department(
                        rs.getInt("DepartmentId"),
                        rs.getString("DepName"));
                Seller seller = new Seller(
                        rs.getInt("Id"),
                        rs.getString("Name"),
                        rs.getString("Email"),
                        rs.getDate("BirthDate"),
                        rs.getDouble("BaseSalary"),
                        dep);

                return seller;

            }

            return null;


        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultset(rs);
        }


    }

    @Override
    public List<Seller> findAll() {
        return null;
    }
}
