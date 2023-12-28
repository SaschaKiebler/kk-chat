package de.saschakiebler;

import de.saschakiebler.service.ChatUIService;
import de.saschakiebler.service.MessageService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import java.util.concurrent.ExecutorService;

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
       
    }

    
}