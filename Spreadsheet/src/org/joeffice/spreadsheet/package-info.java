@TemplateRegistrations({
    @TemplateRegistration(folder = "Office", content = "XlsxTemplate.xlsx", position = 200), // requireProject=false (Not in 7.3)
    @TemplateRegistration(folder = "Office", content = "XlsTemplate.xls", position = 220)
})
package org.joeffice.spreadsheet;

import org.netbeans.api.templates.TemplateRegistration;
import org.netbeans.api.templates.TemplateRegistrations;
