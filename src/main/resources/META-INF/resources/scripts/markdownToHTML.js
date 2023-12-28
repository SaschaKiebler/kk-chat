

function convertMarkdownToHTML(markdown) {
    // Konvertiert Überschriften
    markdown = markdown.replace(/^### (.*$)/gim, '<h3>$1</h3>');
    markdown = markdown.replace(/^## (.*$)/gim, '<h2>$1</h2>');
    markdown = markdown.replace(/^# (.*$)/gim, '<h1>$1</h1>');

    // Konvertiert fett und kursiv
    markdown = markdown.replace(/\*\*(.*)\*\*/gim, '<strong>$1</strong>');
    markdown = markdown.replace(/\*(.*)\*/gim, '<em>$1</em>');

    // Konvertiert Links
    markdown = markdown.replace(/\[(.*?)\]\((.*?)\)/gim, '<a href="$2">$1</a>');

    // Konvertiert Absätze
    markdown = markdown.replace(/\n$/gim, '<br />');

    markdown = convertMarkdownUnorderedLists(markdown);
    markdown = convertMarkdownOrderedList(markdown);
    markdown = convertCodeToCodeBlock(markdown);

    return markdown;
}

function convertMarkdownUnorderedLists(markdown) {
    let inList = false;
    let html = [];

    markdown.split('\n').forEach(line => {
        if (line.trim().startsWith('-')) {
            if (!inList) {
                html.push('<ul>');
                inList = true;
            }
            html.push('<li>' + line.trim().substring(1).trim() + '</li>');
        } else {
            if (inList) {
                html.push('</ul>');
                inList = false;
            }
            html.push(line);
        }
    });

    if (inList) {
        html.push('</ul>');
    }

    return html.join('\n');
}

function convertMarkdownOrderedList(markdown) {
    let inList = false;
    let html = [];

    markdown.split('\n').forEach(line => {
        if (/^\d+\./.test(line.trim())) { 
            if (!inList) {
                html.push('<ol>');
                inList = true;
            }
            html.push('<li>' + line.trim().replace(/^\d+\.\s*/, '') + '</li>'); 
        } else {
            if (inList) {
                html.push('</ol>');
                inList = false;
            }
            html.push(line);
        }
    });

    if (inList) {
        html.push('</ol>');
    }

    return html.join('\n');
}

function convertCodeToCodeBlock(markdown) {
    markdown = markdown.replace(/`{3}(.*)\n([\s\S]*)\n`{3}/gim, '<div class="code"><pre><code class="$1">$2</code></pre></div>');

    return markdown;
}