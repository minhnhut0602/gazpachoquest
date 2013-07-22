package net.sf.gazpachosurvey.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.FetchType;
import javax.persistence.MapKeyClass;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.MapKeyJoinColumns;
import javax.persistence.OneToMany;

import net.sf.gazpachosurvey.domain.support.NamedEntity;
import net.sf.gazpachosurvey.types.Language;

@Entity
public class Survey extends NamedEntity<Integer> {

    private String description;

    private Set<SurveyRunning> surveysRunning;
    
    private Map<Language, SurveyTranslation> translations;

    public Survey() {
        super();
    }

    public Survey(String name) {
        super();
        super.setName(name);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToMany(mappedBy = "survey")
    public Set<SurveyRunning> getSurveysRunning() {
        if (surveysRunning == null) {
            surveysRunning = new HashSet<>();
        }
        return surveysRunning;
    }

    public void setSurveysRunning(Set<SurveyRunning> surveysRunning) {
        this.surveysRunning = surveysRunning;
    }
    
    @OneToMany(mappedBy="survey", cascade=CascadeType.ALL, fetch = FetchType.EAGER)
  //  @MapKeyColumn(name="language_key")
    //@MapKeyClass(Language.class)
    @MapKeyEnumerated(EnumType.STRING)
   @MapKeyColumn(name="language")
    public Map<Language, SurveyTranslation> getTranslations() {
        if (translations == null){
            translations = new HashMap<>();
        }
        return translations;
    }
    /*
    @OneToMany
    @JoinTable(name = "ISSUE_EMPLOYEE",
        joinColumn = @JoinColumn(name = "ISSUE_ID"),
        inverseJoinColumn = @JoinColumn("EMPLOYEE_ID"))
    @MapKeyColumn(name="EMPLOYEEPOSITION_ID") 
    private Map<EmployeePosition, Employee> namedEmployees = new Hashtable<EmployeePosition, Employee>(); 
    */
    public void setTranslation(Language language, String text){
        SurveyTranslation translation = new SurveyTranslation();
        translation.setText(text);
        translation.setSurvey(this);
        translation.setLanguage(language);
        getTranslations().put(language, translation);
    }

    public void setTranslations(Map<Language, SurveyTranslation> translations) {
        this.translations = translations;
    }

    public void addSurveyRunning(SurveyRunning surveyRunning) {
        getSurveysRunning().add(surveyRunning);
        surveyRunning.setSurvey(this);
    }

    public void removeSurveyRunning(SurveyRunning surveyRunning) {
        getSurveysRunning().remove(surveyRunning);
        surveyRunning.setSurvey(null);
    }

    @Override
    public String toString() {
        return "Survey [getId()=" + getId() + ", description=" + description
                + ", name=" + getName() + "]";
    }

}
