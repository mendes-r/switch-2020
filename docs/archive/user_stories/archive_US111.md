# US111 - As a family Administrator, I want to add a category to the family's category tree
=======================================

# 1. Requirements

> __"As a family administrator, I want to add a category to the family’s category tree."__

## 1.1 Description

* The family administrator has the possibility of adding categories to the list of categories of the family. The family
  has a list of standard categories, and the administrator can add customized categories to that list.

**Demo 1** The family administrator has the possibility of adding customized categories to the list of categories of the
family.

- Demo 1.1. The family can't add or edit a standard category.
- Demo 1.3. Root categories designations are unique. It is possible to have categories with same designations as long as they
  have different parents. And parent categories can't have the same categoryDesignation as their child categories.

# 2. Analysis

## 2.1 Product Owner

* Some answers of the product owner (PO) are important in some design decisions.

> It's not possible to add a category that already exists in the tree.

## 2.2 Decisions

* The categories on the list are represented by its categoryDesignation, meaning the list will be filled with Strings and not references to the instance Category.
* For the user, the main identifier will be the categoryDesignation of the category, however in the system each category has is full path
  that works as the main identifier of the category.

## 2.3 Dependent US

* The [US001](../../user_stories/sp1/US001.md)  influences the implementation of this US, since the implementation of the
  custom categories follows the same method of the standard categories.
  
## 2.4 System Sequence Diagram 
* The system sequence diagram shows all the interactions between the family administrator and the system.
```puml


autonumber
skinparam titleFontSize 20
skinparam monochrome true
skinparam maxMessageSize 300
skinparam monochrome true

title US:111 - System Sequence Diagram 
actor "Family Administrator" as fa 
participant System  as sy

activate fa
activate sy 

fa -> sy : Add Category to the family's Category Tree
sy --> fa : request data (parentCategory categoryDesignation)
fa -> sy : input data (parentDesignation, familyId)
sy --> fa : list<String> : fullPathCategories
fa -> sy : fullPathCategory(String)
sy --> fa : request data (newCategory categoryDesignation)
fa -> sy : input data (newCategoryDesignation, familyId)
sy --> fa : newCustomCategoryDTO


```
# 3. Design

## 3.1. Functionalities Flow

```puml


autonumber
skinparam titleFontSize 20
skinparam monochrome true
skinparam maxMessageSize 300
skinparam monochrome true

title SD part1
actor "Family Administrator" as fa
participant UI
participant ":AddCategoryToFamily\nCategoryTreeController" as actc
participant ":FFMApplication" as app
participant ":familyService:\nFamilyService" as fs
participant ":CategoryCommon" as cc
participant "fullPathCategories:List<String>" as fpc

activate fa
fa -> UI : Add category to the\nfamily's category tree
activate UI
     UI --> fa : request data\n(parentDesignation)
     fa -> UI : input data\n(parentDesignation,familyId)
     UI -> actc : findCategory\n(parentDesignation,familyId)
    activate actc

        actc -> app : getFamilyService()
        activate app
        actc <-- app : familyService
        deactivate app
        activate fs
        
        actc -> fs : findFamily(familyId)
        fs --> actc : family
        activate cc
        actc -> cc : findCategory(parentDesignation)  
            loop for each Category in categories
               opt !category.hasSameDesignation()
                 activate fpc
                 cc -> fpc :addFullPathCategory
               end
            end  
            deactivate cc   
        
          
        actc <-- fpc : List<String>fullPathCategories
        deactivate fpc
        deactivate fs
   
    UI <-- actc : List<String>fullPathCategories
    
fa <-- UI : List<String>fullPathCategories
fa -> UI : FullPathParent(String)
fa <-- UI : request category categoryDesignation
fa -> UI : input data (newCategoryDesignation)
UI -> actc : addCustomCategory(newCategoryDesignation,\nfullPathParent,familyId)

actc -> app : getFamilyService()
        activate app
        actc <-- app : familyService
        deactivate app
        activate fs
        actc -> fs : findFamily(familyId)
        fs --> actc : family
        actc --> UI : newCustomCategoryDTO
        UI --> fa : newCustomCategoryDTO

ref over actc
addCustomCategoryToFamilyCategoryTree()
end ref


```

* The part1 of the sequence diagram, shows the overview of the implementation of this user storie. This user storie
  starts with the family administrator trying to add a category to the family's category tree, first the administrator
  has to pass the parent categoryDesignation of the new category that he wants to create, the categoryDesignation and the family ID is
  sent to the controller, that in his turn is going to invoke the Family Service, in order to find the family of the
  administrator given the family ID. Once the family is found is now possible to access the list of categories of the
  family, in order to check if the categoryDesignation of the parent category is in the list of categories of the family. If the
  category exists,then it will return to the family administrator a list with all the full paths of the parent
  category. Then the family administrator will have the possibility of choosing the path where he wants to add the new
  category, and will pass to the UI the categoryDesignation of the new category.

```puml

autonumber
skinparam titleFontSize 20
skinparam monochrome true
skinparam maxMessageSize 300

title addCustomCategoryToFamilyCategoryTree()
participant ":AddCategoryToFamily\nCategoryTreeController" as actc
participant ":CategoryCommon" as cc
participant ":familyService:\nFamilyService" as fs
participant ":family" as fam

alt !fullPathParent=null
activate actc
activate cc
   actc -> cc : getCategoryGivenFullPath()
   cc --> actc : ParentCategory
deactivate cc
activate fs
   actc -> fs : addCustomCategory(newCategoryDesignation,\nparentCategory)
   fs -> fam : addCustomCategory(newCategoryDesignation,\nparentCategory)
activate fam
deactivate fs
   fam --> actc : newCustomCategory
deactivate fam
   [<--actc : newCustomCategoryDTO
else fullPathParent=null
   actc -> fs :addCustomRootCategory(newCategoryDesignation)
activate fs
   fs -> fam :addCustomRootCategory(newCategoryDesignation)
activate fam
deactivate fs
   fam --> actc : newCustomRootCategory
deactivate fam
   [<--actc : newCustomRootCategoryDTO
deactivate actc
end
```

* The controller will receive the String with the full path of the parent category
  and the categoryDesignation of the new category. If the family administrator is trying to add a custom category
  the controller will invoke the addCustomCategory, that will create a custom category and
  add it to the list of categories of the family.
  If the family administrator is trying to add a root category, then the fullPathParent will
  be null, and the controller will invoke the method addCustomRootCategory, that will create a root category and
  add it to the list of categories of the family.
  In either case the new categoryDTO it will be returned to the UI.

## 3.2. Class Diagram
```puml

skinparam monochrome true

class Category {
- id : String
- categoryDesignation : String
- parentCategory : Category
+ hasSameDesignation()
}

class CustomCategory {

}

class Family {
- id : String
- name : String
+ addCustomRootCategory()
+ addCustomCategory()
}

class FamilyService {
+ addCustomRootCategory(String categoryDesignation, String familyId)
+ addCustomCategory(String categoryDesignation, Category parent, String familyId)
+ findFamily(familyId)
}

class addCategoryToFamilyCategoryTreeController {
+ findCategory(parentDesignation,familyId)
+ addCustomCategory(String categoryDesignation,String fullPathParent,String familyId)


}

class FFMApplication {
}

addCategoryToFamilyCategoryTreeController - FFMApplication : familyService
addCategoryToFamilyCategoryTreeController - FamilyService
FFMApplication "1" *-- "1" FamilyService
FamilyService "1" *-- "0..*" Family : categoryList
Family "0..*" *-- "1..*" Category
Category <|-- CustomCategory

```

* As shown in the sequence diagram, Controller that makes the connection between the UI and the business logic. The main
  functionality of the FFMApplication is to delegate the incoming requests to the appropriate Services,in this case,
  FamilyService which contains the list of all the families of the application that in turn contains the list of
  standard categories.

## 3.3. Applied Design Patterns
* From GRASP pattern:
  Controller, Low Coupling, High Cohesion, Pure fabrication and Polymorphism

* From SOLID:
  Single Responsibility Principle and Liskov Substitution Principle

## 3.4. Test
**Test 1:** Adds a custom root category to the list of categories of the family

    @Test
    @DisplayName("Add Custom Root Category")
     public void addCustomRootCategory() {
        FamilyService familyService = this.app.getFamilyService();
        String familyId = familyService.createFamily("Alves");
        CategoryDTO category = new AddCategoryToFamilyCategoryTreeController(app).addCustomCategory("Bills", null, familyId);
        List<Category> categories = familyService.findFamily(familyId).getCategories();
        Assertions.assertEquals(1, categories.size());
        Assertions.assertNotNull(category);
    }

**Test 2:** Adds a custom category to the list of categories of the family

    @Test
    @DisplayName("Add Custom Categories")
    public void addCustomCategories() {
        FamilyService familyService = this.app.getFamilyService();
        String familyId = familyService.createFamily("Sousa");
        AddCategoryToFamilyCategoryTreeController controller = new AddCategoryToFamilyCategoryTreeController(app);
        controller.addCustomCategory("Bills", null, familyId);
        List<Category> categories = familyService.findFamily(familyId).getCategories();
        List<String> fullPath = controller.findCategory("Bills", familyId);
        controller.addCustomCategory("Gas", fullPath.get(0), familyId);
        controller.addCustomCategory("Electricity", fullPath.get(0), familyId);
        controller.addCustomCategory("Fuel", fullPath.get(0), familyId);
        List<String> fullPath1 = controller.findCategory("Fuel", familyId);
        controller.addCustomCategory("Diesel", fullPath1.get(0), familyId);
        Assertions.assertEquals(5, categories.size());
        Assertions.assertEquals(1, fullPath.size());
        Assertions.assertEquals(1, fullPath1.size());
    }
**Test 3:** Adds a custom category to a standard root category

    @Test
    @DisplayName("Add Custom Category to a Standard Root Category")
    public void addCustomCategory() {
        CategoryService categoryService = this.app.getCategoryService();
        FamilyService familyService = this.app.getFamilyService();
        Category root0 = categoryService.createCategory("Transportation");
        Category parent0 = categoryService.createChildCategory("Bus", root0);
        Category parent1 = categoryService.createChildCategory("Train", root0);
        Assertions.assertTrue(root0.getChildren().contains(parent0));
        Assertions.assertTrue(root0.getChildren().contains(parent1));
        String familyId = familyService.createFamily("Constantino");
        AddCategoryToFamilyCategoryTreeController controller = new AddCategoryToFamilyCategoryTreeController(app);
        List<String> fullPath = controller.findCategory("Transportation", familyId);
        CategoryDTO category = controller.addCustomCategory("Metro", fullPath.get(0), familyId);
        List<Category> categories = familyService.findFamily(familyId).getCategories();
        Assertions.assertEquals(4, categories.size());
        Assertions.assertEquals(1, fullPath.size());
        Assertions.assertNotNull(category);
    }
**Test 4:** Adds a custom category to a standard category

     @Test
    @DisplayName("Add Custom Category to a Standard Category")
    public void addCustomCategory2() {
        CategoryService categoryService = this.app.getCategoryService();
        FamilyService familyService = this.app.getFamilyService();
        Category root0 = categoryService.createCategory("Transportation");
        Category parent0 = categoryService.createChildCategory("Bus", root0);
        Category parent1 = categoryService.createChildCategory("Train", root0);
        Assertions.assertTrue(root0.getChildren().contains(parent0));
        Assertions.assertTrue(root0.getChildren().contains(parent1));
        String familyId = familyService.createFamily("Constantino");
        AddCategoryToFamilyCategoryTreeController controller = new AddCategoryToFamilyCategoryTreeController(app);
        List<String> fullPath =controller.findCategory("Transportation", familyId);
        controller.addCustomCategory("Metro", fullPath.get(0), familyId);
        List<String> fullPath1 = controller.findCategory("Bus", familyId);
        controller.addCustomCategory("Monthly", fullPath1.get(0), familyId);
        List<String> fullPath2 = controller.findCategory("Train",familyId);
        controller.addCustomCategory("Monthly", fullPath2.get(0), familyId);
        List<String> fullPath3 = controller.findCategory("Monthly", familyId);
        CategoryDTO category = controller.addCustomCategory("FirstFortnight", fullPath3.get(0), familyId);
        List<Category> categories = familyService.findFamily(familyId).getCategories();
        Assertions.assertEquals(7, categories.size());
        Assertions.assertEquals(1, fullPath.size());
        Assertions.assertEquals(1, fullPath.size());
        Assertions.assertEquals(1, fullPath2.size());
        Assertions.assertEquals(2, fullPath3.size());
        Assertions.assertNotNull(category);
        Family family = familyService.findFamily(familyId);
        UIUtils.displayListCategories(family.getCategories());
    }


* In test 1, is tested the creation and the addition of a root category to the list of categories
  of the family. And in test 2, is possible to test the creation and addition of a custom category to the list
  of categories of the family.

# 4. Implementation
* The implementation of this US, was simple since it follows the same guidelines as [US001](../../user_stories/sp1/US001.md).
  And both tests are a good example of that.
* In this US was implemented inheritance, as the Custom Category class extends from the class Category, since both
  classes share the same attributes.


# 6. Observations
* In this US we use the full path of a category as a unique identifier, however it would also be possible
  to use an ID, with structured rules, to identify each category.
* Instead of using inheritance it would be possible to use a boolean in order to check if the category
  is standard or custom. 