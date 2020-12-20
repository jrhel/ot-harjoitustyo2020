# Testing document

The application includes automated tests for both the application logic and the DAO:s.

## JUnit & integration testing

### Application logic

The application logic, i.e. classes contained by the package [_main.domain_](https://github.com/jrhel/ot-harjoitustyo2020/tree/master/otRunView/src/main/java/main/domain), is tested by corresponding classes in the test package [_domain_](https://github.com/jrhel/ot-harjoitustyo2020/tree/master/otRunView/src/test/java/domain).

### DAO:s

The DAO:s, i.e. classes contained by the package [_main.dao_](https://github.com/jrhel/ot-harjoitustyo2020/tree/master/otRunView/src/main/java/main/dao), are tested by corresponding classes in the test package [_dao_](https://github.com/jrhel/ot-harjoitustyo2020/tree/master/otRunView/src/test/java/dao).

In order for JUnit tests not to interfere with the users data, 1) the applications DAO classes implement a dynamic naming scheme for the database tables they use while retaining the relationship between the DAOs and thus the intactness of the database, 2) JUnit tests use the same data access objects as the application, but initialize the application logic with a different constructor passing on different names for the DAOs to use for their database tables.
Thus the application and JUnit tests use the same application logic and the same database, but parallel database tables instead of the same ones. The uppermost constructor is the constructor used by the application, and the lowermost constructor is used by JUnit tests:

    public Logic() {
        this.runDao = new RunDAO("Run");
        this.catDao = new CategoryDAO("Category");
        this.catAttributeDao = new CategoryAttributeDAO("CategoryAttribute", "Category");
        this.runCatDao = new RunCategoryDAO("RunCategory", "Run", "Category");
        this.ensureDataBaseExists();
    }
    
    public Logic(String runTableName, String categoryTableName, String runCategoryTableName, String categoryAttributeTableName) {
        this.runDao = new RunDAO(runTableName);
        this.catDao = new CategoryDAO(categoryTableName);
        this.catAttributeDao = new CategoryAttributeDAO(categoryAttributeTableName, categoryTableName);
        this.runCatDao = new RunCategoryDAO(runCategoryTableName, runTableName, categoryTableName);
        this.ensureDataBaseExists();
    }
