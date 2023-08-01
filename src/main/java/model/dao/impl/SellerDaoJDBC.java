package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                Department dep = instantiateDeparment(rs);
                Seller seller = instantiateSeller(rs, dep);

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
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {

            st = conn.prepareStatement( "SELECT seller.*,department.Name as DepName " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId = department.Id " +
                    "WHERE DepartmentId = ? " +
                    "ORDER BY Name");

            st.setInt(1,department.getId());

            rs = st.executeQuery();

            List<Seller> listSeller = new ArrayList<>();
            Map<Integer, Department> mapDepartment = new HashMap();


            while (rs.next())
            {
                Department dep = mapDepartment.get(rs.getInt("DepartmentId"));

                if (dep == null)
                {
                    dep = instantiateDeparment(rs);
                    mapDepartment.put(rs.getInt("DepartmentId"), dep);
                }
                Seller seller = instantiateSeller(rs, dep);
                listSeller.add(seller);
            }

            return listSeller;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultset(rs);
        }
    }

    private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException{
        return new Seller(
                rs.getInt("Id"),
                rs.getString("Name"),
                rs.getString("Email"),
                rs.getDate("BirthDate"),
                rs.getDouble("BaseSalary"),
                dep);
    }

    private Department instantiateDeparment(ResultSet rs) throws SQLException {
        return new Department(
                rs.getInt("DepartmentId"),
                rs.getString("DepName"));
    }

    @Override
    public List<Seller> findAll() {
        return null;
    }
}
