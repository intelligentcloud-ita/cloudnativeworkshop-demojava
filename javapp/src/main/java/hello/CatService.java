package hello;
import java.util.Date;
import java.util.List;
public interface CatService {
        void createDb();
        void addCat(String name, Date rescued, Boolean vaccinated);
        void deleteCat(String name, Long id);
        List atriskcats(Date rescued);
        long getGeneratedKey(String name, Date rescued, Boolean vaccinated);
}