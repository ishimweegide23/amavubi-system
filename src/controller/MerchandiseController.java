package controller;

import Model.Merchandise;
import Model.Fan;
import Service.MerchandiseService;
import Dao.MerchandiseDAO;
import java.util.Arrays;
import java.util.Collections;
import view.MerchandisePage;
import java.util.List;

public class MerchandiseController {
    private MerchandiseService merchandiseService;
    private MerchandiseDAO merchandiseDAO;
    private MerchandisePage view;

    public MerchandiseController() {
        this.merchandiseService = new MerchandiseService();
        this.merchandiseDAO = new MerchandiseDAO();
    }

    // Connect to view
    public void setView(MerchandisePage view) {
        this.view = view;
    }

    // Merchandise CRUD operations
    public void saveMerchandise(Merchandise merchandise) {
        try {
            merchandiseService.saveMerchandise(merchandise);
            if (view != null) {
                view.loadMerchandiseData();
            }
        } catch (Exception e) {
            showError("Error saving merchandise: " + e.getMessage());
        }
    }

    public void updateMerchandise(Merchandise merchandise) {
        try {
            merchandiseService.updateMerchandise(merchandise);
            if (view != null) {
                view.loadMerchandiseData();
            }
        } catch (Exception e) {
            showError("Error updating merchandise: " + e.getMessage());
        }
    }

    public void deleteMerchandise(int id) {
        try {
            merchandiseService.deleteMerchandise(id);
            if (view != null) {
                view.loadMerchandiseData();
            }
        } catch (Exception e) {
            showError("Error deleting merchandise: " + e.getMessage());
        }
    }

    // Data retrieval methods
    public List<Merchandise> getAllMerchandise() {
        try {
            return merchandiseService.getAllMerchandise();
        } catch (Exception e) {
            showError("Error loading merchandise: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Merchandise> getMerchandiseByCategory(String category) {
        try {
            return merchandiseService.getMerchandiseByCategory(category);
        } catch (Exception e) {
            showError("Error loading merchandise by category: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public Merchandise getMerchandiseById(int id) {
        try {
            return merchandiseService.getMerchandiseById(id);
        } catch (Exception e) {
            showError("Error getting merchandise: " + e.getMessage());
            return null;
        }
    }

    // Business logic methods
    public boolean purchaseMerchandise(Fan fan, Merchandise merchandise, int quantity, String size) {
        try {
            // Check stock availability
            if (merchandise.getStockQuantity() < quantity) {
                showError("Not enough stock available");
                return false;
            }

            // Update stock
            int newStock = merchandise.getStockQuantity() - quantity;
            boolean stockUpdated = merchandiseService.updateStockQuantity(merchandise.getItemId(), newStock);
            
            if (!stockUpdated) {
                showError("Failed to update stock");
                return false;
            }
            
            return true;
        } catch (Exception e) {
            showError("Purchase failed: " + e.getMessage());
            return false;
        }
    }

    public List<String> getAvailableSizes(int itemId) {
        try {
            String sizes = merchandiseDAO.getAvailableSizes(itemId);
            if (sizes != null && !sizes.equalsIgnoreCase("NULL")) {
                return Arrays.asList(sizes.split("\\s*,\\s*"));
            }
            return Collections.singletonList("One Size");
        } catch (Exception e) {
            showError("Error getting sizes: " + e.getMessage());
            return Collections.singletonList("One Size");
        }
    }

    public boolean checkSizeAvailability(int itemId, String size) {
        try {
            return merchandiseDAO.checkSizeAvailability(itemId, size);
        } catch (Exception e) {
            showError("Error checking size availability: " + e.getMessage());
            return false;
        }
    }

    // View-related methods
    public void loadMerchandisePage(Fan fan) {
        new MerchandisePage(fan).setVisible(true);
    }

    private void showError(String message) {
        if (view != null) {
            view.showErrorMessage(message);
        } else {
            System.err.println(message);
        }
    }
}