package com.application.service.store;

import com.application.entity.kitchen.store.DailyRevenue;
import com.application.entity.kitchen.store.Purchase;
import com.application.entity.kitchen.store.PurchasedBrand;
import com.application.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PurchaseService implements IPurchaseService{

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Override
    public List<Purchase> findAll() {
        return (List<Purchase>) purchaseRepository.findAll();
    }

    @Override
    public void savePurchase(Purchase purchase) {
        purchaseRepository.save(purchase);
    }

    @Override
    public void deletePurchase(Purchase purchase) {
        purchaseRepository.delete(purchase);
    }

    @Override
    public Optional<Purchase> findBPurchase(Long id) {
        return purchaseRepository.findById(id);
    }

    @Override
    public List<DailyRevenue> getDailyRevenue() {
        List<Purchase> purchases = findAll();

        List<DailyRevenue> dailyRevenues = new ArrayList<>();

        for (int i = 0; i < purchases.size(); i++) {
            if (java.time.LocalDateTime.now().getMonthValue() - 1 == purchases.get(i).getDate().getMonth()) {
                DailyRevenue dailyRevenue = new DailyRevenue(purchases.get(i).getDate().getDate(), purchases.get(i).getPurchaseAmount() * purchases.get(i).getQuantity());
                for (int j = 0; j < purchases.size(); j++) {
                    if (purchases.get(j).getDate().getDate() == purchases.get(i).getDate().getDate() && i != j &&
                            java.time.LocalDateTime.now().getMonthValue() - 1 == purchases.get(j).getDate().getMonth()) {
                        dailyRevenue.setRevenue(dailyRevenue.getRevenue() + purchases.get(j).getPurchaseAmount() * purchases.get(j).getQuantity());
                        purchases.remove(j--);
                    }
                }
                purchases.remove(i--);
                dailyRevenues.add(dailyRevenue);
            }
        }
        return dailyRevenues;
    }

    @Override
    public List<List<DailyRevenue>> getDailyRevenueRoomNumberBased() {
        List<Purchase> purchases = findAll();

        List<DailyRevenue> dailyRevenues = new ArrayList<>();

        for (int i = 0; i < purchases.size(); i++) {
            if (java.time.LocalDateTime.now().getMonthValue() - 1 == purchases.get(i).getDate().getMonth()) {
                DailyRevenue dailyRevenue = new DailyRevenue(purchases.get(i).getDate().getDate(), purchases.get(i).getPurchaseAmount() * purchases.get(i).getQuantity(), purchases.get(i).getRoomNumber());
                for (int j = 0; j < purchases.size(); j++) {
                    if (purchases.get(j).getDate().getDate() == purchases.get(i).getDate().getDate() && i != j &&
                            java.time.LocalDateTime.now().getMonthValue() - 1 == purchases.get(j).getDate().getMonth() &&
                        purchases.get(i).getRoomNumber() == purchases.get(j).getRoomNumber()) {
                        dailyRevenue.setRevenue(dailyRevenue.getRevenue() + purchases.get(j).getPurchaseAmount() * purchases.get(j).getQuantity());
                        purchases.remove(j--);
                    }
                }
                purchases.remove(i--);
                dailyRevenues.add(dailyRevenue);
            }
        }

        dailyRevenues.sort(Comparator.comparingInt(DailyRevenue::getDayOfMonth));

        List<List<DailyRevenue>> dailyRevenuesRoomBased = new ArrayList<>();

        for (int i = 0; i < dailyRevenues.size(); i++) {
            List<DailyRevenue> tmpList = new ArrayList<>();
            tmpList.add(dailyRevenues.get(i));
            for (int j = 0; j < dailyRevenues.size(); j++) {
                if (dailyRevenues.get(i).getRoomNumber() == dailyRevenues.get(j).getRoomNumber() && i != j ) {
                    tmpList.add(dailyRevenues.get(j));
                    dailyRevenues.remove(j--);
                }
            }
            if (dailyRevenues.size() != 0) {
                dailyRevenues.remove(i--);
            }
            dailyRevenuesRoomBased.add(tmpList);
        }

        for (int i = 0; i < dailyRevenuesRoomBased.size(); i++) {
            System.out.println("Room Number: " + dailyRevenuesRoomBased.get(i).get(0).getRoomNumber());

            for (int j = 0; j < dailyRevenuesRoomBased.get(i).size(); j++) {
                System.out.println("Date: " + dailyRevenuesRoomBased.get(i).get(j).getDayOfMonth());
            }
        }

        return dailyRevenuesRoomBased;
    }

    @Override
    public List<DailyRevenue> getDailyRevenueBrandBased() {
        List<Purchase> purchases = findAll();

        List<DailyRevenue> dailyRevenues = new ArrayList<>();

        for (int i = 0; i < purchases.size(); i++) {
            if (java.time.LocalDateTime.now().getMonthValue() - 1 == purchases.get(i).getDate().getMonth()) {
                DailyRevenue dailyRevenue = new DailyRevenue(purchases.get(i).getDate().getDate());
                Map<String, Double> dailyPurchasedBrandMap = new HashMap<>();
                dailyPurchasedBrandMap.put(purchases.get(i).getBrand(), purchases.get(i).getPurchaseAmount() * purchases.get(i).getQuantity());
                for (int j = 0; j < purchases.size(); j++) {
                    if (purchases.get(j).getDate().getDate() == purchases.get(i).getDate().getDate() && i != j &&
                            java.time.LocalDateTime.now().getMonthValue() - 1 == purchases.get(j).getDate().getMonth()) {
                        if (dailyPurchasedBrandMap.get(purchases.get(j).getBrand()) != null) {
                            dailyPurchasedBrandMap.put(purchases.get(j).getBrand(), dailyPurchasedBrandMap.get(purchases.get(j).getBrand()) + purchases.get(j).getPurchaseAmount() * purchases.get(j).getQuantity());
                        }
                        else {
                            dailyPurchasedBrandMap.put(purchases.get(j).getBrand(), purchases.get(j).getPurchaseAmount() * purchases.get(j).getQuantity());
                        }
                        purchases.remove(j--);
                    }
                }
                purchases.remove(i--);
                dailyRevenue.setBrandRevenues(dailyPurchasedBrandMap);
                dailyRevenues.add(dailyRevenue);
            }
        }

        for (DailyRevenue dailyRevenue : dailyRevenues) {
           for (String brand : getBrands()) {
                boolean brandPurchased = false;
                Iterator itr=dailyRevenue.getBrandRevenues().keySet().iterator();
                while (itr.hasNext()) {
                    String key = (String) itr.next();
                    if (key.equals(brand)) {
                        System.out.println("Brand: " + brand);
                        brandPurchased = true;
                    }
                }
                if (!brandPurchased) {
                    dailyRevenue.getBrandRevenues().put(brand, (double) 0);
                }
            }
            dailyRevenue.setBrandRevenues(sortHashMap(dailyRevenue.getBrandRevenues()));

        }

        return dailyRevenues;
    }

    private List<String> getBrands() {
        List<String> brands = new ArrayList<>();

        brands.add("Carlsberg");
        brands.add("Heineken");
        brands.add("Tuborg");
        brands.add("Royal");
        brands.add("Coke");
        brands.add("Wine");
        brands.add("Vodka");
        brands.add("Fanta");
        brands.add("Pepsi");
        brands.add("Egekilde");
        brands.add("Faxe Kondi");
        brands.add("Other");

        brands.sort(String::compareTo);

        return brands;
    }

    private Map<String, Double> sortHashMap(Map<String, Double> map) {
        List list = new LinkedList(map.entrySet());

        Collections.sort(list, new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                return ((Comparable) ((Map.Entry) (o1)).getKey()).compareTo(((Map.Entry) (o2)).getKey());
            }
        });

        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();)
        {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

    @Override
    public List<PurchasedBrand> getQuantityPurchasedBrandBased() {
        List<PurchasedBrand> purchasedBrands = new ArrayList<>();

        List<Purchase> purchases = findAll();

        for (int i = 0; i < purchases.size(); i++) {


            if (java.time.LocalDateTime.now().getMonthValue() - 1 == purchases.get(i).getDate().getMonth()) {

                PurchasedBrand purchasedBrand = new PurchasedBrand(purchases.get(i).getBrand(), purchases.get(i).getQuantity());
                for (int j = 0; j < purchases.size(); j++) {
                    if (purchases.get(j).getBrand().equals(purchasedBrand.getBrand()) && i != j &&
                            java.time.LocalDateTime.now().getMonthValue() - 1 == purchases.get(j).getDate().getMonth()) {
                        purchasedBrand.setPurchasedAmount(purchasedBrand.getPurchasedAmount() + purchases.get(j).getQuantity());
                        purchases.remove(j--);
                    }
                }
                purchases.remove(i--);
                purchasedBrands.add(purchasedBrand);
            }
        }
        return purchasedBrands;
    }

    @Override
    public double getTotalRevenueCurrentMonth() {
        List<Purchase> purchases = findAll();

        double totalRevenue = 0;

        for (Purchase purchase : purchases) {
            if (java.time.LocalDateTime.now().getMonthValue() - 1 == purchase.getDate().getMonth()) {
                totalRevenue += purchase.getPurchaseAmount() * purchase.getQuantity();
            }
        }

        return totalRevenue;
    }
}
