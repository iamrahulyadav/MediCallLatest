package themedicall.com.MediPediaGetterSetter;

/**
 * Created by Muhammad Adeel on 3/2/2018.
 */

public class BrandFormsAndSimilarMedicineGetterSetter {

    private String name;
    private String potency;
    private String companyName;
    private String drugName;

    public BrandFormsAndSimilarMedicineGetterSetter(String name, String potency, String companyName, String drugName) {
        this.name = name;
        this.potency = potency;
        this.companyName = companyName;
        this.drugName = drugName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPotency() {
        return potency;
    }

    public void setPotency(String potency) {
        this.potency = potency;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }
}

