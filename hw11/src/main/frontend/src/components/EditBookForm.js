import React, { useState } from 'react';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    TextField,
    Button,
    FormControl,
    InputLabel,
    Select,
    MenuItem,
    Alert
} from '@mui/material';
import * as api from '../services/api';

const EditBookForm = ({ book, authors, genres, open, onClose, onSave }) => {
    const [formData, setFormData] = useState({
        id: book?.id || '',
        title: book?.title || '',
        authorId: book?.author?.id || '',
        genreId: book?.genre?.id || ''
    });
    const [error, setError] = useState('');

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        console.log('Submitting form with data:', formData);
        try {
            const updatedBook = await api.updateBook(book.id, formData);
            onSave(updatedBook);
            onClose();
        } catch (err) {
            setError(err.message);
        }
    };

    return (
        <Dialog 
            open={open} 
            onClose={onClose}
            maxWidth="md"
            fullWidth
        >
            <DialogTitle>Редактировать книгу</DialogTitle>
            <DialogContent>
                {error && (
                    <Alert severity="error" sx={{ mb: 2 }} onClose={() => setError('')}>
                        {error}
                    </Alert>
                )}
                <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '20px', marginTop: '10px', minWidth: '500px' }}>
                    <TextField
                        label="Название"
                        name="title"
                        value={formData.title}
                        onChange={handleChange}
                        required
                        fullWidth
                    />
                    <FormControl fullWidth required>
                        <InputLabel>Автор</InputLabel>
                        <Select
                            name="authorId"
                            value={formData.authorId}
                            onChange={handleChange}
                            label="Автор"
                        >
                            {authors.map(author => (
                                <MenuItem key={author.id} value={author.id}>
                                    {author.name}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                    <FormControl fullWidth required>
                        <InputLabel>Жанр</InputLabel>
                        <Select
                            name="genreId"
                            value={formData.genreId}
                            onChange={handleChange}
                            label="Жанр"
                        >
                            {genres.map(genre => (
                                <MenuItem key={genre.id} value={genre.id}>
                                    {genre.name}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                </form>
            </DialogContent>
            <DialogActions>
                <Button onClick={onClose}>Отмена</Button>
                <Button onClick={handleSubmit} variant="contained" color="primary">
                    Сохранить
                </Button>
            </DialogActions>
        </Dialog>
    );
};

export default EditBookForm; 