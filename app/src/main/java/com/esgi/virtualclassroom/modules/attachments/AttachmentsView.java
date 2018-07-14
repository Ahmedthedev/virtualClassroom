package com.esgi.virtualclassroom.modules.attachments;

import java.io.File;

interface AttachmentsView {
    void updateAttachmentsList();
    void openFile(File file);
    void exit();
}
