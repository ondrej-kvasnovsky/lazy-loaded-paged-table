package lazyloadedpagedtable;

import org.junit.Assert;

import java.util.List;

/**
 * @author Ondrej Kvasnovsky
 */
public class AgreementDAOTest {

    @org.junit.Test
    public void testCount() throws Exception {
        AgreementDAO dao = new AgreementDAO();
        int count = dao.count(new AgreementSearchCriteria());
        Assert.assertEquals(1000, count);
    }

    @org.junit.Test
    public void testFindFirst10Items() throws Exception {
        AgreementDAO dao = new AgreementDAO();
        List<Agreement> agreements = dao.find(new AgreementSearchCriteria(), 0, 10, null);
        Assert.assertEquals(10, agreements.size());
        Assert.assertEquals(0, agreements.get(0).getId());
    }

    @org.junit.Test
    public void testFind10_20() throws Exception {
        AgreementDAO dao = new AgreementDAO();
        List<Agreement> agreements = dao.find(new AgreementSearchCriteria(), 10, 10, null);
        Assert.assertEquals(10, agreements.size());
        Assert.assertEquals(10, agreements.get(0).getId());
    }
}
