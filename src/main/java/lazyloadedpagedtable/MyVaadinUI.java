package lazyloadedpagedtable;

import com.jensjansson.pagedtable.ControlsLayout;
import com.jensjansson.pagedtable.PagedTable;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.addons.lazycontainer.LazyBeanContainer;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class MyVaadinUI extends UI
{

    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);

        PagedTable table = new PagedTable("PagedTable Example");
        ControlsLayout controls = table.createControls();

        LazyBeanContainer container = new LazyBeanContainer(Agreement.class, new AgreementDAO(), new AgreementSearchCriteria());
        table.setContainerDataSource(container);

        table.setWidth("500px");
        controls.setWidth("500px");

        layout.addComponent(table);
        layout.addComponent(controls);
    }

}
