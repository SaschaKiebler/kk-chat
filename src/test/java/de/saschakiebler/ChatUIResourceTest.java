

package de.saschakiebler;

import io.quarkus.qute.Template;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseEventSink;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.saschakiebler.enums.MessageRoles;
import de.saschakiebler.resource.ChatUIResource;
import de.saschakiebler.service.ChatUIService;

import static org.mockito.Mockito.*;

@QuarkusTest
@ExtendWith(MockitoExtension.class)
class ChatUIResourceTest {

    @Mock
    Template chat;

    @Mock
    ChatUIService chatService;

    @Mock
    Sse sse;

    @Mock
    SseEventSink eventSink;

    @InjectMocks
    ChatUIResource chatUIResource;


@BeforeEach
void setup() {
    chat = mock(Template.class);
    chatService = mock(ChatUIService.class);
    chatUIResource = new ChatUIResource(chat, chatService);
}

    @Test
    void testGet() {
        when(chat.data(anyString(), any())).thenReturn(null);
        chatUIResource.get();
        verify(chat).data(eq("messages"), anyList());
    }

    @Test
    void testStreamAnswer() {
        String messageText = "test message";
        eventSink = mock(SseEventSink.class);
        sse = mock(Sse.class);
        Multi<String> responseStream = Multi.createFrom().items("test message");
        when(chatService.streamAnswer(messageText)).thenReturn(responseStream);
        chatUIResource.streamAnswer(messageText, eventSink, sse);
        verify(chatService).safeMessage(messageText, MessageRoles.USER);
        verify(chatService).streamAnswer(messageText);
    }
}