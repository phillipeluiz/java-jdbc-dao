package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import javax.xml.transform.Result;
import java.sql.*;
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
    public Integer insert(Seller seller) {
        Integer id = null;
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                "INSERT INTO seller " +
                   "(Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                        "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

           st.setString(1, seller.getName());
           st.setString(2, seller.getEmail());
           st.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()) );
           st.setDouble(4, seller.getBaseSalary());
           st.setInt(5, seller.getDepartment().getId());


           int rowsAffected = st.executeUpdate();

           if (rowsAffected > 0)
           {
               ResultSet rs = st.getGeneratedKeys();
               while (rs.next())
               {
                   id = rs.getInt(1);
               }
               DB.closeResultset(rs);
           }
           else
           {
              throw new DbException("0 Rows affected. Error when inserted");
           }

            return  id;

        }
        catch (SQLException e)
        {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }

    }

    @Override
    public void update(Seller seller) {
        Integer id = null;
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "UPDATE seller SET Name = ?," +
                            " Email = ?," +
                            " BirthDate = ?," +
                            " BaseSalary = ?," +
                            " DepartmentId = ?" +
                            " WHERE Id = ?"
                    );

            st.setString(1, seller.getName());
            st.setString(2, seller.getEmail());
            st.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()) );
            st.setDouble(4, seller.getBaseSalary());
            st.setInt(5, seller.getDepartment().getId());
            st.setInt(6, seller.getId());

            st.executeUpdate();

        }
        catch (SQLException e)
        {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }
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

    @Override
    public List<Seller> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {

            st = conn.prepareStatement( "SELECT seller.*,department.Name as DepName " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId = department.Id " +
                    "ORDER BY Name");

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

}
