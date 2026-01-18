import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.net.URI;
import com.google.gson.*;




public class Oikonomia{
   

   private static final Gson gson = new GsonBuilder()
    .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) -> 
        new JsonPrimitive(src.toString()))
    .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfObject, context) -> 
        LocalDate.parse(json.getAsString()))
    .setPrettyPrinting()
    .create();   
   
   private static final String FILE_NAME = "pantry.json";

   static int iconWidth = 30;
   static int iconHeight = 30;

   static ImageIcon originalAddIcon = new ImageIcon("src/assets/Heavy Plus Sign Emoji.png");
   static ImageIcon addIcon = new ImageIcon(originalAddIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH));

   static ImageIcon originalViewIcon = new ImageIcon("src/assets/File Cabinet Emoji.png");
   static ImageIcon viewIcon = new ImageIcon(originalViewIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH));

   static ImageIcon originalIngredientIcon = new ImageIcon("src/assets/carrot.png");
   static ImageIcon ingredientIcon = new ImageIcon(originalIngredientIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH));

   static ImageIcon originalFindIcon = new ImageIcon("src/assets/magnifying glass.png");
   static ImageIcon findIcon = new ImageIcon(originalFindIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH));

   static ImageIcon originalDeleteIcon = new ImageIcon("src/assets/x.png");
   static ImageIcon deleteIcon = new ImageIcon(originalDeleteIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH));

   static ImageIcon originalBackIcon = new ImageIcon("src/assets/return arrow.png");
   static ImageIcon backIcon = new ImageIcon(originalBackIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH));

   static ImageIcon originalYearIcon = new ImageIcon("src/assets/One Emoji.png");
   static ImageIcon yearIcon = new ImageIcon(originalYearIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH));

   static ImageIcon originalMonthIcon = new ImageIcon("src/assets/Two Emoji.png");
   static ImageIcon monthIcon = new ImageIcon(originalMonthIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH));

   static ImageIcon originalDayIcon = new ImageIcon("src/assets/three.png");
   static ImageIcon dayIcon = new ImageIcon(originalDayIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH));

   static ImageIcon originalSubmitIcon = new ImageIcon("src/assets/check mark.png");
   static ImageIcon submitIcon = new ImageIcon(originalSubmitIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH));

   public static void saveIngredients(ArrayList<Ingredient> pantry, String FILE_NAME) {
       try (java.io.FileWriter writer = new java.io.FileWriter(FILE_NAME)) {
           gson.toJson(pantry, writer);
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
   public static ArrayList<Ingredient> loadIngredients(String FILE_NAME) {
       ArrayList<Ingredient> pantry = new ArrayList<>();
       try (java.io.FileReader reader = new java.io.FileReader(FILE_NAME)) {
           Ingredient[] ingredientsArray = gson.fromJson(reader, Ingredient[].class);
           if (ingredientsArray != null) {
               for (Ingredient ingredient : ingredientsArray) {
                   pantry.add(ingredient);
               }
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
       return pantry;
   }

   public static void main(String[] args){

         //creates pantry to store ingredients
         ArrayList<Ingredient> pantry = loadIngredients(FILE_NAME);

         //creates main frame
         JFrame frame = new JFrame("Oikonomia Ingredient Manager");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setSize(550, 300);
         frame.getContentPane().setLayout(new GridLayout(0, 1));

         //adds operation panel with two buttons for operations 
         JPanel operationPanel = new JPanel();
         operationPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
         JButton addIngredientButton = new JButton("Add Ingredient");
         operationPanel.add(addIngredientButton);
         addIngredientButton.setIcon(addIcon);
         JButton viewIngredientsButton = new JButton("View Ingredients");
         operationPanel.add(viewIngredientsButton);
         viewIngredientsButton.setIcon(viewIcon);
         operationPanel.setLayout(new GridLayout(0, 1));

         //adds panel for the process of adding an ingredient
         JPanel addIngredientPanel = new JPanel();
         addIngredientPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
            //text field for name of ingredient
            JTextField nameField = new JTextField(10);
            addIngredientPanel.add(new JLabel("Ingredient Name:",ingredientIcon,SwingConstants.LEFT));
            addIngredientPanel.add(nameField);

            //text fields for expiration date of ingredient
            //text field for expiration year of ingredient
            JTextField expirationYearField = new JTextField(10);
            addIngredientPanel.add(new JLabel("Estimated Expiration Year (yyyy):",yearIcon,SwingConstants.LEFT));
            addIngredientPanel.add(expirationYearField);
            expirationYearField.setText(LocalDate.now().getYear() + "");
            
            //text field for expiration month of ingredient
            JTextField expirationMonthField = new JTextField(10);
            addIngredientPanel.add(new JLabel("Estimated Expiration  Month (mm):",monthIcon,SwingConstants.LEFT));
            addIngredientPanel.add(expirationMonthField);
            expirationMonthField.setText(String.format("%02d", LocalDate.now().getMonthValue()));

            //text field for expiration day of ingredient
            JTextField expirationDayField = new JTextField(10);
            addIngredientPanel.add(new JLabel("Estimated Expiration  Day (dd):",dayIcon,SwingConstants.LEFT));   
            addIngredientPanel.add(expirationDayField);
            expirationDayField.setText(String.format("%02d", LocalDate.now().getDayOfMonth()));

            //button to find expiration date online
            JButton findExpirationButton = new JButton("Not Sure? Find Expiration Date");
            addIngredientPanel.add(findExpirationButton);
            findExpirationButton.setIcon(findIcon);
            findExpirationButton.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                  try {
                     String query = "average shelf life of " + nameField.getText();
                     String url = "https://www.google.com/search?q=" + query.replace(" ", "+");
                     if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().browse(new URI(url));
                     }
                  } catch (Exception ex) {
                     ex.printStackTrace();
                  }
               }
            });

            //button to submit the new ingredient
            JButton submitIngredientButton = new JButton("Submit Ingredient");
            addIngredientPanel.add(submitIngredientButton);
            submitIngredientButton.setIcon(submitIcon);
            addIngredientPanel.setLayout(new GridLayout(6, 1));
            
            //back button to return to home screen
            JPanel backPanel = new JPanel();
            JButton backButton = new JButton("Return to Home");
            backPanel.add(backButton);
            backButton.setIcon(backIcon);
            backPanel.setLayout(new GridLayout(1,1));
            addIngredientPanel.add(backPanel);
            addIngredientPanel.setLayout(new GridLayout(6, 1));
            addIngredientPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
            
            
            //action listener for add ingredient button that makes the add ingredient panel visible in the frame beneath the operation panel
            addIngredientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               frame.getContentPane().removeAll();
               frame.getContentPane().add(addIngredientPanel);
               frame.revalidate();
               frame.repaint();
            }
         });
         //action listener for submit ingredient button that clears text fields in add ingredient panel and adds values to new Ingredient object
         submitIngredientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               String name = nameField.getText();
               LocalDate expirationDate = LocalDate.parse(
                  (String.format("%04d", Integer.parseInt(expirationYearField.getText()))+
                  "-"+(String.format("%02d", Integer.parseInt(expirationMonthField.getText()))+
                  "-"+(String.format("%02d", Integer.parseInt(expirationDayField.getText()))))));
               Ingredient newIngredient = new Ingredient(name, expirationDate);
               pantry.add(newIngredient);
               saveIngredients(pantry, FILE_NAME);
               //clear text fields
               nameField.setText("");
               expirationYearField.setText(LocalDate.now().getYear() + "");
               expirationMonthField.setText(String.format("%02d", LocalDate.now().getMonthValue()));
               expirationDayField.setText(String.format("%02d", LocalDate.now().getDayOfMonth()));
            }
         });
         //action listener for back button that returns to home screen
         backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               frame.getContentPane().removeAll();
               frame.getContentPane().add(operationPanel);
               frame.revalidate();
               frame.repaint();
            }
         });

         //action listener for view ingredients button that displays all ingredients in pantry sorted by most recent expiration date as discrete panels with a button to find recipes online using that ingredient
         viewIngredientsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               frame.getContentPane().removeAll();
               frame.getContentPane().setLayout(new BorderLayout());
               loadIngredients(FILE_NAME);
               
               //sort pantry by expiration date
               pantry.sort((i1, i2) -> i1.expirationDate.compareTo(i2.expirationDate));
               
               //create panel to hold ingredient panels
               JPanel viewIngredientsPanel = new JPanel();
               viewIngredientsPanel.setLayout(new BoxLayout(viewIngredientsPanel, BoxLayout.Y_AXIS));
               
               //create and add individual ingredient panels
               for(Ingredient ingredient : pantry){
                  JPanel ingredientPanel = new JPanel();
                  ingredientPanel.setMinimumSize(new Dimension(0, 180));
                  ingredientPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                  ingredientPanel.add(new JLabel(ingredient.name + " | Expiration Date: " + ingredient.expirationDate.toString()));
                  
                  //add button to find recipes online using that ingredient
                  JButton findRecipes = new JButton("Find Recipes");
                  ingredientPanel.add(findRecipes);
                  findRecipes.setIcon(findIcon);
                  findRecipes.addActionListener(new ActionListener() {
                     @Override
                     public void actionPerformed(ActionEvent e) {
                        try {
                           String query1 = "recipes with " + ingredient.name;
                           String url1 = "https://www.google.com/search?q=" + query1.replace(" ", "+");
                           String query2 = "how to use leftover " + ingredient.name;
                           String url2 = "https://www.google.com/search?q=" + query2.replace(" ", "+");
                           if (Desktop.isDesktopSupported()) {
                              Desktop.getDesktop().browse(new URI(url1));
                              Desktop.getDesktop().browse(new URI(url2));
                           }
                        } catch (Exception ex) {
                           ex.printStackTrace();
                        }

                     }


                  });

                  //add button to delete ingredient from pantry
                  JButton deleteIngredient = new JButton("Delete Ingredient");
                  ingredientPanel.add(deleteIngredient);
                  deleteIngredient.setIcon(deleteIcon);
                  deleteIngredient.addActionListener(new ActionListener() {
                     @Override
                     public void actionPerformed(ActionEvent e) {
                        pantry.remove(ingredient);
                        saveIngredients(pantry, FILE_NAME);
                     }
                  });
                  ingredientPanel.setLayout(new GridLayout(0, 1));
                  viewIngredientsPanel.add(ingredientPanel);


               }
            JScrollPane scrollPane = new JScrollPane(viewIngredientsPanel);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            
            //add back button to return to home
            JPanel fixedNavPanel = new JPanel();
            JButton backButton = new JButton("Return to Home");
            backButton.setIcon(backIcon);
            fixedNavPanel.add(backButton);

            frame.revalidate();
            backButton.addActionListener(new ActionListener() {
                  @Override
                  public void actionPerformed(ActionEvent e) {
                     frame.getContentPane().removeAll();
                     frame.getContentPane().setLayout(new GridLayout(0, 1));
                     frame.getContentPane().add(operationPanel);
                     frame.revalidate();
                     frame.repaint();
                  }
            });
            frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
            frame.getContentPane().add(fixedNavPanel, BorderLayout.SOUTH);
            frame.revalidate();
            frame.repaint()
            ;
            }

         });


         frame.getContentPane().add(operationPanel);
         frame.setVisible(true);


   }



}