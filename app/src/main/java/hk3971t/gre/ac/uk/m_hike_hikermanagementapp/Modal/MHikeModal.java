package hk3971t.gre.ac.uk.m_hike_hikermanagementapp.Modal;

public class MHikeModal {
    private String hiking_name, location, date, level_of_difficulty, description, parking_available, observation, observation_time, observation_comment;
    private Integer length_of_hike, special_requirements, recommended_gear, observation_id, observation_ref;

    public Integer getSpecial_requirements() {
        return special_requirements;
    }

    public void setSpecial_requirements(Integer special_requirements) {
        this.special_requirements = special_requirements;
    }

    public Integer getId() {
        return id;
    }

    public Integer getObservation_id() {
        return observation_id;
    }

    public void setObservation_id(Integer observation_id) {
        this.observation_id = observation_id;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getObservation_time() {
        return observation_time;
    }

    public void setObservation_time(String observation_time) {
        this.observation_time = observation_time;
    }

    public String getObservation_comment() {
        return observation_comment;
    }

    public void setObservation_comment(String observation_comment) {
        this.observation_comment = observation_comment;
    }

    public Integer getObservation_ref() {
        return observation_ref;
    }

    public void setObservation_ref(Integer observation_ref) {
        this.observation_ref = observation_ref;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getId(int anInt) {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private Integer id;

    public String getHiking_name() {
        return hiking_name;
    }

    public void setHiking_name(String hiking_name) {
        this.hiking_name = hiking_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLevel_of_difficulty() {
        return level_of_difficulty;
    }

    public void setLevel_of_difficulty(String level_of_difficulty) {
        this.level_of_difficulty = level_of_difficulty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParking_available() {
        return parking_available;
    }

    public void setParking_available(String parking_available) {
        this.parking_available = parking_available;
    }

    public Integer getRecommended_gear() {
        return recommended_gear;
    }

    public void setRecommended_gear(Integer recommended_gear) {
        this.recommended_gear = recommended_gear;
    }

    public Integer getLength_of_hike() {
        return length_of_hike;
    }

    public void setLength_of_hike(Integer length_of_hike) {
        this.length_of_hike = length_of_hike;
    }

    public Integer getNumber_of_dogs() {
        return special_requirements;
    }

    public void setNumber_of_dogs(Integer number_of_dogs) {
        this.special_requirements = number_of_dogs;
    }

}
