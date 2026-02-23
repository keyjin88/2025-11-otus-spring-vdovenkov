const handleStreamResponse = async (response) => {
    if (!response.ok) {
        const error = await response.text();
        throw new Error(error || 'Произошла ошибка при выполнении запроса');
    }
    const reader = response.body.getReader();
    const decoder = new TextDecoder();
    const items = [];

    while (true) {
        const { done, value } = await reader.read();
        if (done) break;
        
        const chunk = decoder.decode(value, { stream: true });
        console.log('Chunk received:', chunk);
        const lines = chunk.split('\n').filter(line => line.trim());
        
        for (const line of lines) {
            try {
                const item = JSON.parse(line);
                console.log('Parsed item:', item);
                items.push(item);
            } catch (e) {
                console.warn('Failed to parse JSON line:', line);
            }
        }
    }
    
    console.log('Final items:', items);
    return items;
};

const handleResponse = async (response) => {
    if (!response.ok) {
        const error = await response.text();
        throw new Error(error || 'Произошла ошибка при выполнении запроса');
    }
    const contentType = response.headers.get('content-type');
    if (contentType && contentType.includes('application/json')) {
        return response.json();
    }
    return null;
};

export const fetchBooks = async () => {
    const response = await fetch('/api/books');
    const contentType = response.headers.get('content-type');
    if (contentType && contentType.includes('application/x-ndjson')) {
        return handleStreamResponse(response);
    }
    return handleResponse(response);
};

export const fetchAuthors = async () => {
    const response = await fetch('/api/authors');
    const contentType = response.headers.get('content-type');
    if (contentType && contentType.includes('application/x-ndjson')) {
        return handleStreamResponse(response);
    }
    return handleResponse(response);
};

export const fetchGenres = async () => {
    const response = await fetch('/api/genres');
    const contentType = response.headers.get('content-type');
    if (contentType && contentType.includes('application/x-ndjson')) {
        return handleStreamResponse(response);
    }
    return handleResponse(response);
};

export const createBook = async (bookData) => {
    const response = await fetch('/api/books', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(bookData)
    });
    return handleResponse(response);
};

export const updateBook = async (id, bookData) => {
    const response = await fetch(`/api/books/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(bookData)
    });
    return handleResponse(response);
};

export const deleteBook = async (id) => {
    const response = await fetch(`/api/books/${id}`, {
        method: 'DELETE'
    });
    return handleResponse(response);
}; 