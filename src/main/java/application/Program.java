package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Program {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println(" ==== Teste 1: findById ======");
        Seller seller = sellerDao.findById(3);
        System.out.println(seller);

        System.out.println("\n ==== Teste 2: findByDepartment ============");
        List<Seller> listSeller = sellerDao.findByDepartment(new Department(2,null));

        for ( Seller item: listSeller ) {
            System.out.println(item);
        }

        System.out.println("\n ==== Teste 3: findAll ============");
        listSeller = sellerDao.findAll();

        for (Seller item: listSeller) {
            System.out.println(item);
        }

        System.out.println("Deseja inserir o registro novamente? (y/n)");
        String resposta= sc.next();
        if (resposta.equals("y")) {
            System.out.println("\n ==== Teste 4: insert ============");
            Department dep = new Department(2, null);
            Seller sellerNew = new Seller(0, "Phillipe", "phillipe@email.com", new Date(), 15000.00, dep);
            Integer id = sellerDao.insert(sellerNew);
            System.out.println("Registro inserido com sucesso. Id: " + id);
        }

        System.out.println("\n ==== Teste 5: Update ============");
        seller = sellerDao.findById(3);
        sellerDao.update(seller);
        System.out.println("Registro atualizado com sucesso.");

        System.out.println("Indique o código a ser removido, para ignorar digite 0 (zero)");
        Integer idSeller = sc.nextInt();
        if (idSeller >0)
        {
            System.out.println("\n ==== Teste 6: Delete  ============");
            sellerDao.deleteById(idSeller);
            System.out.println("Registro deletado com sucesso.");
        }




    }
}
