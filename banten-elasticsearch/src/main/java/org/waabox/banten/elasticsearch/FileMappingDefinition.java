package org.waabox.banten.elasticsearch;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;

/** A file mapping definition that allows using of a file in order to
 * generate the mapping and settings.
 *
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class FileMappingDefinition implements MappingDefinition {

  /** The index name, it's never null.*/
  private final String indexName;

  /** The index type name, it's never null.*/
  private final String indexType;

  /** The mapping file name, it's never null.*/
  private final String mappingFileName;

  /** The settings file name, it's never null.*/
  private final String settingsFileName;

  /** Creates a new instance of the {@link MappingDefinition}.
  *
  * @param theIndexName the index name, cannot be null.
  * @param theIndexType the index type, cannot be null.
  * @param theMappingFilename the mapping file name, cannot be null.
  */
 public FileMappingDefinition(
     final String theIndexName,
     final String theIndexType,
     final String theMappingFilename) {
   this(theIndexName, theIndexType, theMappingFilename, null);
 }

  /** Creates a new instance of the {@link MappingDefinition}.
   *
   * @param theIndexName the index name, cannot be null.
   * @param theIndexType the index type, cannot be null.
   * @param theMappingFilename the mapping file name, cannot be null.
   * @param theSettingsFileName the settings file name, can be null.
   */
  public FileMappingDefinition(
      final String theIndexName,
      final String theIndexType,
      final String theMappingFilename,
      final String theSettingsFileName) {

    Validate.notNull(theIndexName, "The index name cannot be null");
    Validate.notNull(theIndexType, "The index type cannot be null");
    Validate.notNull(theMappingFilename , "The mapping file cannot be null");

    indexName = theIndexName;
    indexType = theIndexType;
    mappingFileName = theMappingFilename;
    settingsFileName = theSettingsFileName;
  }

  /** {@inheritDoc}.*/
  @Override
  public String indexName() {
    return indexName;
  }

  /** {@inheritDoc}.*/
  @Override
  public String indexType() {
    return indexType;
  }

  /** {@inheritDoc}.*/
  @Override
  public String getMapping() throws IOException {
    String mapping = fileToString(mappingFileName);
    Validate.notNull(mapping, "Mapping for:" + indexName + " not found");
    return mapping;
  }

  /** {@inheritDoc}.*/
  @Override
  public String getSettings() throws IOException {
    if (settingsFileName == null) {
      return null;
    }
    return fileToString(settingsFileName);
  }

  /** Finds the given file-name within the classpath an converts it into an
   * string representation.
   *
   * @param fileName the file-name, cannot be null.
   *
   * @return the {@link String} representation, can be null if file not found.
   */
  private String fileToString(final String fileName) {
    Validate.notNull(fileName, "The file name cannot be null");
    InputStream file;
    file = getClass().getClassLoader().getResourceAsStream(fileName);
    try {
      return IOUtils.toString(file);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      IOUtils.closeQuietly(file);
    }
  }

}
