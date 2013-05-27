/*
 * Copyright 2013 Japplis.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.joeffice.desktop.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.joeffice.desktop.ui.OfficeTopComponent;

import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.cookies.SaveCookie;
import org.openide.cookies.ViewCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.*;
import org.openide.nodes.Children;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.windows.CloneableTopComponent;

/**
 * Generic data object for the office documents.
 * This class is abstract as sub class should implement opening and saving a document.
 *
 * @author Anthony Goubard - Japplis
 */
public abstract class OfficeDataObject extends MultiDataObject implements SaveCookie, SaveAsCapable, CookieSet.Factory {

    private Object document;

    private OfficeOpenSupport opener;

    public OfficeDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException {
        super(pf, loader);
        CookieSet cookies = getCookieSet();
        cookies.add(OfficeOpenSupport.class, this);
        cookies.add(SaveCookie.class, this);
        cookies.assign(SaveAsCapable.class, this);
    }

    public void setDocument(Object document) {
        this.document = document;
    }

    public Object getDocument() {
        return document;
    }

    @Override
    public void setModified(boolean modified) {
        super.setModified(modified);
        if (modified) {
            getCookieSet().add(this);
        } else {
            getCookieSet().remove(this);
        }
    }

    @Override
    protected Node createNodeDelegate() {
        return new DataNode(this, Children.LEAF, getLookup());
    }

    @Override
    public Lookup getLookup() {
        return getCookieSet().getLookup();
    }

    @Override
    public <T extends Node.Cookie> T createCookie(Class<T> type) {
        if (type.isAssignableFrom(OfficeOpenSupport.class)) {
            if (opener == null) {
                opener = new OfficeOpenSupport(getPrimaryEntry());
            }
            return (T) opener;
        }
        if (type.isAssignableFrom(SaveCookie.class)) {
            return (T) this;
        }
        return null;
    }

    @Override
    protected int associateLookup() {
        return 1;
    }

    @Override
    public synchronized void save() throws IOException {
        File currentFile = FileUtil.toFile(getPrimaryFile());
        File backup = backupOriginal(currentFile);
        File secureSave = new File(currentFile.getAbsolutePath() + ".new." + getPrimaryFile().getExt());
        save(secureSave);
        if (secureSave.exists() && secureSave.length() > 0) {
            try (OutputStream currentFileStream = new FileOutputStream(currentFile)) {
                Files.copy(secureSave.toPath(), currentFileStream);
            }
            boolean deleted = backup.delete();
            boolean newDeleted = secureSave.delete();
        }
        setModified(false);
    }

    private synchronized File backupOriginal(File currentFile) throws IOException {
        File backupFile = new File(currentFile.getAbsolutePath() + ".backup." + getPrimaryFile().getExt());
        if (!backupFile.exists() && currentFile.length() > 0) {
            Files.copy(currentFile.toPath(), backupFile.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
        }
        return backupFile;
    }

    @Override
    public void saveAs(FileObject folder, String fileName) throws IOException {
        FileObject newFile = folder.getFileObject(fileName);
        if (newFile == null) {
            newFile = folder.createData(fileName);
        }
        save(FileUtil.toFile(newFile));
    }

    public abstract OfficeTopComponent open(OfficeDataObject dataObject);

    public abstract void save(File file) throws IOException;

    private class OfficeOpenSupport extends OpenSupport implements OpenCookie, CloseCookie, ViewCookie {

        private OfficeOpenSupport(MultiDataObject.Entry entry) {
            super(entry);
        }

        @Override
        protected CloneableTopComponent createCloneableTopComponent() {
            OfficeDataObject dataObject = (OfficeDataObject) entry.getDataObject();
            return OfficeDataObject.this.open(dataObject);
        }
    }
}
