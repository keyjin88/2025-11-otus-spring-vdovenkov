import React, { useState, useEffect } from 'react';
import {
    TextField,
    Button,
    FormControl,
    InputLabel,
    Select,
    MenuItem,
    Box,
    Typography,
    Alert,
    Collapse
} from '@mui/material';
import { EMPTY_BOOK } from '../types';
import * as api from '../services/api';

const AddBookForm = ({ onBookAdded }) => {
    const [formData, setFormData] = useState(EMPTY_BOOK);
    const [authors, setAuthors] = useState([]);
    const [genres, setGenres] = useState([]);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState(false);

    useEffect(() => {
        loadAuthorsAndGenres();
    }, []);

    const loadAuthorsAndGenres = async () => {
        try {
            const [authorsData, genresData] = await Promise.all([
                api.fetchAuthors(),
                api.fetchGenres()
            ]);
            console.log('Loaded authors:', authorsData);
            console.log('Loaded genres:', genresData);
            setAuthors(authorsData);
            setGenres(genresData);
        } catch (err) {
            setError('Ошибка при загрузке справочных данных');
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        console.log(`Changing ${name} to ${value}`);
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const resetForm = () => {
        setFormData(EMPTY_BOOK);
        setError('');
        setSuccess(true);
        setTimeout(() => setSuccess(false), 3000);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        console.log('Submitting form with data:', formData);
        try {
            const newBook = await api.createBook(formData);
            onBookAdded(newBook);
            resetForm();
        } catch (err) {
            setError(err.message);
        }
    };

    return (
        <Box component="form" onSubmit={handleSubmit} sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
            <Typography variant="h6" component="h3">
                Добавить новую книгу
            </Typography>

            <Collapse in={Boolean(error)}>
                <Alert severity="error" onClose={() => setError('')}>
                    {error}
                </Alert>
            </Collapse>

            <Collapse in={success}>
                <Alert severity="success">
                    Книга успешно добавлена
                </Alert>
            </Collapse>

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
                            {author ? author.name : 'Неизвестный автор'}
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
                            {genre ? genre.name : 'Неизвестный жанр'}
                        </MenuItem>
                    ))}
                </Select>
            </FormControl>

            <Button type="submit" variant="contained" color="primary">
                Добавить
            </Button>
        </Box>
    );
};

export default AddBookForm; 