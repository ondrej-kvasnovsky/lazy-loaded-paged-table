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
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);

        PagedTable table = new PagedTable("PagedTable Example");
        table.setWidth("500px");
        ControlsLayout controls = table.createControls();
        controls.setWidth("500px");

        LazyBeanContainer container = new LazyBeanContainer(Agreement.class, new AgreementDAO(), new AgreementSearchCriteria());
        table.setContainerDataSource(container);

        layout.addComponent(table);
        layout.addComponent(controls);
        setContent(layout);
    }

}
