/**
 * @autor hecaigui
 * @date 2020-1-31
 * @description
 */
public class PersonBeanImpl implements PersonBean {
    String name;
    String gender;
    String HotOrNotRating;

    @Override
    public String getName() {
        return name;
    }
    @Override
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String getGender() {
        return gender;
    }
    @Override
    public void setGender(String gender) {
        this.gender = gender;
    }
    @Override
    public String getHotOrNotRating() {
        return HotOrNotRating;
    }
    @Override
    public void setHotOrNotRating(String hotOrNotRating) {
        HotOrNotRating = hotOrNotRating;
    }
}
