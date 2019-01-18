package birt;

import java.io.File;
import java.io.IOException;
import java.util.List;

import models.ReportCsv;

public interface CsvGenerator {
	File genrate(List<ReportCsv> list) throws IOException;
}
