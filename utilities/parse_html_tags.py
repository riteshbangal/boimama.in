def parse_html_to_text(file_path):
    with open(file_path, 'r', encoding='utf-8') as file:
        html_content = file.read()
        
    # print(html_content)
        
    # Parse HTML content
    modified_text = html_content.replace("<span>", "") \
                                .replace("</span>", "") \
                                .replace("<br>", "\n") \
                                .replace('<span class="blane" data-suggestions="', '') \
                                .replace('">', '') \
                                .replace('&quot;', '') \
                                .replace('&nbsp;', '') \
                                .replace('<p style="', '') \
                                .replace('</p>', '') \
                                .replace('<b>', '') \
                                .replace('</b>', '') \
                                .replace('<i>', '') \
                                .replace('</i>', '') \
                                .replace('<u>', '') \
                                .replace('</u>', '') \
                                .replace('text-align:LEFT', '') \
                                .replace('text-align:CENTER', '') \
                                .replace('</b>', '') \
    

    return modified_text

file_path = './input.txt'
parsed_text = parse_html_to_text(file_path)

# Write to a text file
with open('output.txt', 'w', encoding='utf-8') as file:
    file.write(parsed_text)

print("Modified text has been written to 'output.txt'")
