# Testing document

The application includes automated tests for both the application logic and the DAO:s. The application has also been tested manually on a system level.

## JUnit & integration testing

### Application logic

The application logic, i.e. classes contained by the package [_main.domain_](https://github.com/jrhel/ot-harjoitustyo2020/tree/master/otRunView/src/main/java/main/domain), is tested by corresponding classes in the test package [_domain_](https://github.com/jrhel/ot-harjoitustyo2020/tree/master/otRunView/src/test/java/domain).

The _Category_ class is exempted form JUnit & integration testing since it only contains getter & setter methods.

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

## System testing

All functionality listed in the [reqiremets specification](https://github.com/jrhel/ot-harjoitustyo2020/blob/master/documentation/Requirements%20specification.md) has been tested, and established to be working as intended. User choices and inputs which have shown to be impossible for the application logic to handle have been prevented. The application will in such cases inform the user of the problem preventing their intended action.

# Known problems and undesired functionality in certain situations

- Currently, the application can not handle an incorrect date being submitted by the user.
- Choosing to edit a run and then closing the form for entering new data regarding the run, without saving, will delete the run.
