package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;
import java.util.List;

public class Program {

    public static void main(String[] args) {

        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println(" ==== Teste 1: findById ======");
        Seller seller = sellerDao.findById(3);
        System.out.println(seller);

        System.out.println("\n ==== Teste 2: findByDepartment ============");
        List<Seller> listSeller = sellerDao.findByDepartment(new Department(2,null));

        for ( Seller item: listSeller ) {
            System.out.println(item);
        }

    }
}
