package com.esgi.virtualclassroom.modules.classroom;

import com.esgi.virtualclassroom.data.models.Classroom;
import com.esgi.virtualclassroom.modules.attachment.AttachmentsFragment;
import com.esgi.virtualclassroom.modules.chat.ChatFragment;

interface ClassroomView {
    void showChatFragment(ChatFragment fragment);
    void showAttachmentFragment(AttachmentsFragment fragment);
    void updateView(Classroom classroom);
    void clearDrawing();
    void startSpeech();
    void stopSpeech();
    void exit();
}
