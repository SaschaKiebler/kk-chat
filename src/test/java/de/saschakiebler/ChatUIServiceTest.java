package de.saschakiebler;

import de.saschakiebler.enums.MessageRoles;
import de.saschakiebler.model.Conversation;
import de.saschakiebler.model.Message;
import de.saschakiebler.service.ChatUIService;
import de.saschakiebler.service.MessageService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class ChatUIServiceTest {

    @Inject
    ChatUIService chatUIService;

    @Mock
    ExecutorService executorService;

    @Mock
    MessageService messageService;

    @Test
    void testSafeMessageInConversation() {
        // given
        String messageText = "Hello";
        MessageRoles sender = MessageRoles.USER;
        Conversation conversation = new Conversation();

        // when
        Message result = chatUIService.safeMessageInConversation(messageText, sender, conversation);

        // then
        Mockito.verify(messageService).createMessage(messageText, sender, conversation);
        assertEquals(sender.getRole(), result.getSender());
        assertEquals(messageText, result.getText());
        assertEquals(conversation, result.getConversation());
    }

    // @Test
    // void testStreamAnswer() {
    //     // given
    //     String messageText = "Hello";
    //     Long conversationId = 1L;
    //     Multi<String> expectedResponseStream = Multi.createFrom().items("response1", "response2");

    //     // mock dependencies
    //     StreamingChatLanguageModel model = Mockito.mock(StreamingChatLanguageModel.class);
    //     when(model.generate(any(), any())).thenAnswer(expectedResponseStream);
    //     when(executorService.submit(any())).thenReturn(null);

    //     // when
    //     Multi<String> responseStream = chatUIService.streamAnswer(messageText, conversationId);

    //     // then
    //     assertEquals(expectedResponseStream, responseStream);
    //     Mockito.verify(messageService).createMessage(any(), any(), any());
    // }
}