package model.dao;

import model.entities.Department;
import model.entities.Seller;

import java.util.List;

public interface SellerDao {
    void insert(Seller department);
    void update(Seller department);
    void deleteById(Integer id);
    Department findById(Integer id);

    List<Seller> findAll();






}
